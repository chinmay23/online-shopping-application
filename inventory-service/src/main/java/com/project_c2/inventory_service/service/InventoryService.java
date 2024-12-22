package com.project_c2.inventory_service.service;

import com.project_c2.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    public boolean isInStock(String skuCode, Integer quantity) {

        log.info("Start -- Received request to check stock for skuCode {}, with quantity {}",skuCode,quantity);
        //find inventory for a given skucode where quantity >=0
        boolean isInStock= inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode,quantity);
        log.info("End -- Product with skuCode {}, and quantity {} is in stock - {}",skuCode,quantity,isInStock);
        return isInStock;
    }
}
