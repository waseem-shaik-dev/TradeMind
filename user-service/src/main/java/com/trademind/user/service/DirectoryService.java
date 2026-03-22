package com.trademind.user.service;

import com.trademind.user.dto.MerchantCardDto;
import com.trademind.user.dto.RetailerCardDto;
import org.springframework.data.domain.Page;

public interface DirectoryService {

    Page<RetailerCardDto> getRetailers(int page, int size, String keyword);

    Page<MerchantCardDto> getMerchants(int page, int size, String keyword);

}