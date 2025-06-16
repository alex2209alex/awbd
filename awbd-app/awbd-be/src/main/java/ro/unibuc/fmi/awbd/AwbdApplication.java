package ro.unibuc.fmi.awbd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableFeignClients(basePackages = "ro.unibuc.fmi.awbd.feign")
@EnableFeignClients
@EnableDiscoveryClient
public class AwbdApplication {
	public static void main(String[] args) {
		SpringApplication.run(AwbdApplication.class, args);
	}
}
