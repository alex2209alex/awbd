package com.awbd.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class GatewayApplication {
    private static final String BE_LOAD_BALANCER = "lb://AWBD";
    private static final String SEGMENT = "/${segment}";

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("subscription_route", r -> r
                        .path("/awbd/subscription/**")
                        .filters(f -> f.rewritePath("/awbd/subscription/(?<segment>/*)", SEGMENT))
                        .uri("lb://SUBSCRIPTION")
                )
                .route("clients_route", r -> r
                        .path("/awbd/clients/**")
                        .filters(f -> f.rewritePath("/awbd/clients/(?<segment>/*)", SEGMENT))
                        .uri(BE_LOAD_BALANCER) // load balancer for be application from eureka
                )
                .route("cooks_route", r -> r
                        .path("/awbd/cooks/**")
                        .filters(f -> f.rewritePath("/awbd/cooks/(?<segment>/*)", SEGMENT))
                        .uri(BE_LOAD_BALANCER) // load balancer for be application from eureka
                )
                .route("couriers_route", r -> r
                        .path("/awbd/couriers/**")
                        .filters(f -> f.rewritePath("/awbd/couriers/(?<segment>/*)", SEGMENT))
                        .uri(BE_LOAD_BALANCER) // load balancer for be application from eureka
                )
                .route("ingredients_route", r -> r
                        .path("/awbd/ingredients/**")
                        .filters(f -> f.rewritePath("/awbd/ingredients/(?<segment>/*)", SEGMENT))
                        .uri(BE_LOAD_BALANCER) // load balancer for be application from eureka
                )
                .route("online_orders_route", r -> r
                        .path("/awbd/online-orders/**")
                        .filters(f -> f.rewritePath("/awbd/online-orders/(?<segment>/*)", SEGMENT))
                        .uri(BE_LOAD_BALANCER) // load balancer for be application from eureka
                )
                .route("producers_route", r -> r
                        .path("/awbd/producers/**")
                        .filters(f -> f.rewritePath("/awbd/producers/(?<segment>/*)", SEGMENT))
                        .uri(BE_LOAD_BALANCER) // load balancer for be application from eureka
                )
                .route("products_route", r -> r
                        .path("/awbd/products/**")
                        .filters(f -> f.rewritePath("/awbd/products/(?<segment>/*)", SEGMENT))
                        .uri(BE_LOAD_BALANCER) // load balancer for be application from eureka
                )
                .route("users_route", r -> r
                        .path("/awbd/users/**")
                        .filters(f -> f.rewritePath("/awbd/users/(?<segment>/*)", SEGMENT))
                        .uri(BE_LOAD_BALANCER) // load balancer for be application from eureka
                )
                .build();
    }
}