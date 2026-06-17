package com.k8s_gateway_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class K8sGatewayApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(K8sGatewayApiApplication.class, args);
	}

	@GetMapping("/api/v1/status")
    public Map<String, String> getStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("version", "1.0.0");
        status.put("message", "Hello from behind the Kubernetes Gateway API!");
        return status;
    }

}
