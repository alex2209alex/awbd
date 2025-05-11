package ro.unibuc.fmi.awbd.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "gateway", fallback = GatewayFallback.class)
public interface GatewayClient {
    @GetMapping("/some-endpoint")  // Endpoint in Gateway to call
    String getGatewayData();
}
