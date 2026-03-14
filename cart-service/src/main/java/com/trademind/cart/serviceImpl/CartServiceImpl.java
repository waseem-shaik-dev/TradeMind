package com.trademind.cart.serviceImpl;

import com.trademind.cart.client.CatalogueClient;
import com.trademind.cart.client.InventoryClient;
import com.trademind.cart.dto.*;
import com.trademind.cart.entity.*;
import com.trademind.cart.enums.*;
import com.trademind.cart.mapper.CartItemMapper;
import com.trademind.cart.mapper.CartMapper;
import com.trademind.cart.mapper.CartPriceMapper;
import com.trademind.cart.repository.CartItemRepository;
import com.trademind.cart.repository.CartRepository;
import com.trademind.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private static final int MAX_CARTS = 10;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CatalogueClient catalogueClient;
    private final InventoryClient inventoryClient;
    private final CartItemMapper cartItemMapper;
    private final CartPriceMapper cartPriceMapper;
    private final CartMapper cartMapper;


    // --------------------------------------------------
    // CART FETCH
    // --------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public UserCartListResponseDto getUserCarts(Long userId) {

        List<Cart> carts =
                cartRepository.findAllByUserIdAndActiveTrue(userId);

        List<CartSummaryResponseDto> summaries =
                carts.stream()
                        .map(cart -> {
                            CartSourceDto source =
                                    cartMapper.mapSource(cart);

                            CartPriceSummaryDto price =
                                    cartPriceMapper.fromCartItems(cart.getItems());


                            return cartMapper.toSummary(cart, source, price);
                        })
                        .toList();

        return new UserCartListResponseDto(
                userId,
                MAX_CARTS,
                carts.size(),
                summaries
        );
    }


    @Override
    @Transactional(readOnly = true)
    public CartResponseDto getCartById(Long userId, Long cartId) {

        Cart cart = getCartOrThrow(userId, cartId);

        List<CartItem> cartItems = cart.getItems();

        if (cartItems.isEmpty()) {
            CartValidationDto validation =
                    validateCart(cart);
            return cartMapper.toCartResponse(
                    cart,
                    cartMapper.mapSource(cart),
                    List.of(),
                    cartPriceMapper.toPriceSummary(List.of()),
                    validation
            );
        }

        List<Long> productIds =
                cartItems.stream()
                        .map(CartItem::getProductId)
                        .toList();

        // 🔹 Batch catalogue fetch
        var productMap =
                catalogueClient.getProducts(productIds)
                        .stream()
                        .collect(java.util.stream.Collectors.toMap(
                                CatalogueClient.CatalogueProductDto::getProductId,
                                p -> p
                        ));

        // 🔹 Batch inventory fetch
        var availabilityMap =
                inventoryClient.getAvailabilityForProducts(productIds)
                        .stream()
                        .collect(java.util.stream.Collectors.toMap(
                                InventoryClient.InventoryAvailabilityDto::productId,
                                InventoryClient.InventoryAvailabilityDto::quantityAvailable
                        ));



        List<CartItemResponseDto> items =
                cartItems.stream()
                        .map(item -> {
                            var product = productMap.get(item.getProductId());

                            if (product == null) {
                                return cartItemMapper.toUnavailableDto(item);
                            }

                            return cartItemMapper.toDto(
                                    item,
                                    product,
                                    availabilityMap.getOrDefault(item.getProductId(), 0)
                            );
                        })

                        .toList();

        CartPriceSummaryDto price =
                cartPriceMapper.toPriceSummary(items);

        CartSourceDto source =
                cartMapper.mapSource(cart);

        CartValidationDto validation =
                validateCart(cart);

        return cartMapper.toCartResponse(
                cart,
                source,
                items,
                price,
                validation
        );

    }


    @Override
    public CartResponseDto getActiveCartBySource(
            Long userId,
            Long sourceId,
            String sourceType
    ) {
        Cart cart = cartRepository
                .findByUserIdAndSourceIdAndSourceTypeAndStatus(
                        userId,
                        sourceId,
                        SourceType.valueOf(sourceType),
                        CartStatus.ACTIVE
                )
                .orElseThrow(() -> new RuntimeException("Active cart not found"));

        return  getCartById(userId, cart.getId());

    }

    // --------------------------------------------------
    // ADD TO CART
    // --------------------------------------------------

    @Override
    public CartItemQuantityResponseDto addToCart(
            Long userId,
            AddToCartRequestDto request
    ) {



        if (request.quantity() == null || request.quantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        Cart cart = cartRepository
                .findByUserIdAndSourceIdAndSourceTypeAndStatus(
                        userId,
                        request.sourceId(),
                        SourceType.valueOf(request.sourceType()),
                        CartStatus.ACTIVE
                )
                .orElseGet(() -> createCart(userId, request));

        CatalogueClient.CatalogueProductDto product =
                catalogueClient.getProduct(request.productId());

        if (!product.getSourceId().equals(request.sourceId())) {
            throw new RuntimeException("Product does not belong to this source");
        }

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), request.productId())
                .orElse(null);

        if (item == null) {
            item = CartItem.builder()
                    .productId(request.productId())
                    .unitPrice(product.getPrice())
                    .quantity(0)
                    .available(true)
                    .build();

            cart.addItem(item); // bidirectional
            item = cartItemRepository.save(item);
        }

        Integer availableQty =
                inventoryClient.getAvailableQuantity(request.productId());

        int finalQty = item.getQuantity() + request.quantity();

        if (availableQty < finalQty) {
            throw new RuntimeException(
                    "Insufficient stock. Available: " + availableQty
            );
        }

        item.setQuantity(finalQty);


        System.out.println("cart item id : "+item.getId()+"\n\n\n\n\n\n\n\n\n\n\n");



        return new CartItemQuantityResponseDto(
                cart.getId(),
                item.getId(),
                request.productId(),
                item.getQuantity()
        );
    }


    // --------------------------------------------------
    // UPDATE CART ITEM
    // --------------------------------------------------

    @Override
    public CartItemQuantityResponseDto updateCartItem(
            Long userId,
            UpdateCartItemRequestDto request
    ) {

        if (request.quantity() == null) {
            throw new RuntimeException("Quantity is required");
        }

        if (request.quantity() < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }

        CartItem item = cartItemRepository.findById(request.cartItemId())
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Cart cart = getCartOrThrow(userId, item.getCart().getId());

        // 🗑️ Quantity = 0 → remove item
        if (request.quantity() == 0) {
            cart.removeItem(item);
            cartRepository.save(cart);

            return new CartItemQuantityResponseDto(
                    cart.getId(),
                    request.cartItemId(),
                    item.getProductId(),
                    0
            );
        }

        Integer availableQty =
                inventoryClient.getAvailableQuantity(item.getProductId());

        if (availableQty < request.quantity()) {
            throw new RuntimeException(
                    "Insufficient stock. Available: " + availableQty
            );
        }

        item.setQuantity(request.quantity());

        return new CartItemQuantityResponseDto(
                cart.getId(),
                item.getId(),
                item.getProductId(),
                item.getQuantity()
        );
    }


    // --------------------------------------------------
    // REMOVE CART ITEM
    // --------------------------------------------------

    @Override
    public void removeCartItem(Long userId, Long cartItemId) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Cart cart = getCartOrThrow(userId, item.getCart().getId());

        cart.removeItem(item);
        cartRepository.save(cart);
    }


    // --------------------------------------------------
    // CLEAR / DELETE CART
    // --------------------------------------------------

    @Override
    public void clearCart(Long userId, Long cartId) {
        Cart cart = getCartOrThrow(userId, cartId);
        cart.getItems().clear(); // triggers orphan removal
        cartRepository.save(cart);

    }

    @Override
    public void deleteCart(Long userId, Long cartId) {
        Cart cart = getCartOrThrow(userId, cartId);
        cart.setActive(false);
        cart.setStatus(CartStatus.ABANDONED);
        cartRepository.save(cart);
    }

    // --------------------------------------------------
    // VALIDATION
    // --------------------------------------------------

    @Override
    public CartValidationDto validateCart(Long userId, Long cartId) {

        Cart cart = getCartOrThrow(userId, cartId);

        // 1️⃣ Empty cart is technically valid (but not checkoutable)
        if (cart.getItems().isEmpty()) {
            return new CartValidationDto(
                    false,
                    false,
                    false,
                    false,
                    "Cart is empty"
            );
        }

        // 2️⃣ Build productId → cartQuantity map
        var cartQuantityMap =
                cart.getItems().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                CartItem::getProductId,
                                CartItem::getQuantity
                        ));

        List<Long> productIds = cartQuantityMap.keySet().stream().toList();

        // 3️⃣ Fetch inventory availability (batch)
        var availabilityList =
                inventoryClient.getAvailabilityForProducts(productIds);

        var availabilityMap =
                availabilityList.stream()
                        .collect(java.util.stream.Collectors.toMap(
                                InventoryClient.InventoryAvailabilityDto::productId,
                                InventoryClient.InventoryAvailabilityDto::quantityAvailable
                        ));

        boolean hasOutOfStock = false;
        boolean exceedsQuantityLimit = false;
        StringBuilder messageBuilder = new StringBuilder();

        // 4️⃣ Validate each cart item
        for (CartItem item : cart.getItems()) {

            Integer availableQty =
                    availabilityMap.get(item.getProductId());

            // Inventory record missing
            if (availableQty == null) {
                hasOutOfStock = true;
                messageBuilder.append("Product ")
                        .append(item.getProductId())
                        .append(" is unavailable. ");
                continue;
            }

            // Completely out of stock
            if (availableQty <= 0) {
                hasOutOfStock = true;
                messageBuilder.append("Product ")
                        .append(item.getProductId())
                        .append(" is out of stock. ");
                continue;
            }

            // Requested quantity exceeds available stock
            if (availableQty < item.getQuantity()) {
                exceedsQuantityLimit = true;
                messageBuilder.append("Only ")
                        .append(availableQty)
                        .append(" units available for product ")
                        .append(item.getProductId())
                        .append(". ");
            }
        }

        boolean valid =
                !hasOutOfStock && !exceedsQuantityLimit;

        String message =
                valid
                        ? "Cart is valid"
                        : messageBuilder.toString().trim();

        return new CartValidationDto(
                valid,
                hasOutOfStock,
                false,              // price mismatch can be added later
                exceedsQuantityLimit,
                message
        );
    }



    // --------------------------------------------------
    // INTERNAL HELPERS
    // --------------------------------------------------

    private Cart createCart(Long userId, AddToCartRequestDto request) {

        long activeCount = cartRepository.countByUserIdAndActiveTrue(userId);
        if (activeCount >= MAX_CARTS) {
            throw new RuntimeException("Maximum cart limit exceeded");
        }

        Cart cart = Cart.builder()
                .userId(userId)
                .sourceId(request.sourceId())
                .sourceType(SourceType.valueOf(request.sourceType()))
                .status(CartStatus.ACTIVE)
                .active(true)
                .build();

        return cartRepository.save(cart);
    }

    private Cart getCartOrThrow(Long userId, Long cartId) {
        return cartRepository.findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }


    @Override
    public void lockCartForCheckout(Long userId, Long cartId) {

        Cart cart = cartRepository
                .findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        if (cart.getStatus() == CartStatus.LOCKED ||
                cart.getStatus() == CartStatus.ORDERED) {
            return; // idempotent
        }

        if (!cart.isActive()) {
            throw new IllegalStateException("Cart is not active");
        }

        cart.setStatus(CartStatus.LOCKED);
        cart.setActive(false);
    }

    @Override
    public void markCartCompleted(Long userId, Long cartId) {

        Cart cart = cartRepository
                .findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        cart.setStatus(CartStatus.ORDERED);
        cart.setActive(false);
    }

    @Override
    public void unlockCartAfterCheckoutFailure(Long cartId) {

        cartRepository.findById(cartId).ifPresent(cart -> {

            if (cart.getStatus() != CartStatus.LOCKED) {
                return;
            }

            cart.setStatus(CartStatus.ACTIVE);
            cart.setActive(true);
        });
    }


    // --------------------------------------------------
// INTERNAL VALIDATION (NO DB RE-FETCH)
// --------------------------------------------------

    private CartValidationDto validateCart(Cart cart) {

        if (cart.getItems().isEmpty()) {
            return new CartValidationDto(
                    false,
                    false,
                    false,
                    false,
                    "Cart is empty"
            );
        }

        // Build productId → quantity map
        var cartQuantityMap =
                cart.getItems().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                CartItem::getProductId,
                                CartItem::getQuantity
                        ));

        List<Long> productIds =
                cartQuantityMap.keySet().stream().toList();

        // Batch inventory fetch
        var availabilityList =
                inventoryClient.getAvailabilityForProducts(productIds);

        var availabilityMap =
                availabilityList.stream()
                        .collect(java.util.stream.Collectors.toMap(
                                InventoryClient.InventoryAvailabilityDto::productId,
                                InventoryClient.InventoryAvailabilityDto::quantityAvailable
                        ));

        boolean hasOutOfStock = false;
        boolean exceedsQuantityLimit = false;
        StringBuilder messageBuilder = new StringBuilder();

        for (CartItem item : cart.getItems()) {

            Integer availableQty =
                    availabilityMap.get(item.getProductId());

            if (availableQty == null) {
                hasOutOfStock = true;
                messageBuilder.append("Product ")
                        .append(item.getProductId())
                        .append(" unavailable. ");
                continue;
            }

            if (availableQty <= 0) {
                hasOutOfStock = true;
                messageBuilder.append("Product ")
                        .append(item.getProductId())
                        .append(" is out of stock. ");
                continue;
            }

            if (availableQty < item.getQuantity()) {
                exceedsQuantityLimit = true;
                messageBuilder.append("Only ")
                        .append(availableQty)
                        .append(" units available for product ")
                        .append(item.getProductId())
                        .append(". ");
            }
        }

        boolean valid =
                !hasOutOfStock && !exceedsQuantityLimit;

        String message =
                valid
                        ? "Cart is valid"
                        : messageBuilder.toString().trim();

        return new CartValidationDto(
                valid,
                hasOutOfStock,
                false,      // price mismatch placeholder
                exceedsQuantityLimit,
                message
        );
    }



}
