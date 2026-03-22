package com.trademind.user.controller;

import com.trademind.user.dto.MerchantCardDto;
import com.trademind.user.dto.RetailerCardDto;
import com.trademind.user.service.DirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users/directory")
@RequiredArgsConstructor
public class DirectoryController {

    private final DirectoryService directoryService;

    @GetMapping("/retailers")
    public Page<RetailerCardDto> getRetailers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String keyword
    ) {
        return directoryService.getRetailers(page, size, keyword);
    }

    @GetMapping("/merchants")
    public Page<MerchantCardDto> getMerchants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String keyword
    ) {
        return directoryService.getMerchants(page, size, keyword);
    }
}