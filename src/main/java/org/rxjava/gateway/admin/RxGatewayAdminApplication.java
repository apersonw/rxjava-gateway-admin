package org.rxjava.gateway.admin;

import org.rxjava.api.manager.inner.InnerLoginInfoApi;
import org.rxjava.apikit.client.ClientAdapter;
import org.rxjava.common.core.api.ReactiveHttpClientAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author happy 2019-03-17 22:10
 */
@SpringBootApplication
public class RxGatewayAdminApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(RxGatewayAdminApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/user/**").filters(p -> p.stripPrefix(1)).uri("http://localhost:8081"))
//                .route(r -> r.path("/user/**").filters(p -> p.stripPrefix(1)).uri("http://rxjava-service-user"))
//                .route(r -> r.path("/goods/**").filters(p -> p.stripPrefix(1)).uri("http://rxjava-service-goods"))
                .route(r -> r.path("/goods/**").filters(p -> p.stripPrefix(1)).uri("http://localhost:8082"))
                .route(r -> r.path("/order/**").filters(p -> p.stripPrefix(1)).uri("http://rxjava-service-order"))
                .route(r -> r.path("/pay/**").filters(p -> p.stripPrefix(1)).uri("http://rxjava-service-pay"))
                .build();
    }

    @Bean
    @Qualifier("userClientAdapter")
    public ClientAdapter userClientAdapter(
            @Qualifier("webFluxConversionService")
                    ConversionService conversionService,
            WebClient.Builder webClientBuilder
    ) {
        return ReactiveHttpClientAdapter.build(
                conversionService, webClientBuilder, "localhost:8081"
        );
    }

    @Bean
    public InnerLoginInfoApi innerLoginInfoApi(@Qualifier("userClientAdapter") ClientAdapter clientAdapter) {
        InnerLoginInfoApi api = new InnerLoginInfoApi();
        api.setclientAdapter(clientAdapter);
        return api;
    }
}