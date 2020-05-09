package tim6.bsep.Gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.WebFilterChain;

@SpringBootApplication
public class GatewayApplication {

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("pki_certificates", r -> r.path("/api/v1/certificates/**")
						.filters(f -> f.setRequestHeader("Authorization", ""))
						.uri("lb://pki"))
				.route("keycloak", r -> r.path("/auth/**")
						.uri("http://localhost:8180/"))
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
