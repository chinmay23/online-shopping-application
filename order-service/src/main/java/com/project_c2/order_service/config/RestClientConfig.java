package com.project_c2.order_service.config;

import com.project_c2.order_service.client.InventoryClient;
import io.micrometer.observation.ObservationRegistry;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;
@Data
@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;
    private final ObservationRegistry observationRegistry;
    @Bean
    public InventoryClient inventoryClient() {
        RestClient restClient=RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .requestFactory(getClientRequestFactory())
                .observationRegistry(observationRegistry)
                .build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpsServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpsServiceProxyFactory.createClient(InventoryClient.class);

    }

    private ClientHttpRequestFactory getClientRequestFactory() {
        ClientHttpRequestFactorySettings clientHttpRequestFactorySettings =ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(5))
                .withReadTimeout(Duration.ofSeconds(5));
        return ClientHttpRequestFactories.get(clientHttpRequestFactorySettings);
    }
}
