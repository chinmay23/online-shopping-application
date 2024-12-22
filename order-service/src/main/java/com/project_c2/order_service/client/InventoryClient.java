package com.project_c2.order_service.client;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.slf4j.Logger;
//@FeignClient(value="inventory",url="${inventory.url}")

public interface InventoryClient {
    Logger log = LoggerFactory.getLogger(InventoryClient.class);
    @GetExchange("/api/inventory")
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
    @Retry(name="inventory")
    //@RequestMapping(method=RequestMethod.GET,value="/api/inventory")
    boolean isInStock(@RequestParam String skuCode,@RequestParam Integer quantity) ;
    default boolean fallbackMethod(String code, Integer quantity,Throwable throwable) {
        log.info("Cannot get inventory for skuCode {} , failure reason: {}",code,throwable.getMessage());
        return false;
    }
}
