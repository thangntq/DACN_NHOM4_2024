package com.ManShirtShop;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;




@SpringBootApplication
@EnableCaching
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	// public WebMvcConfigurer corsConfigurer() {
	// return new WebMvcConfigurer() {
	// @Override
	// public void addCorsMappings(CorsRegistry registry) {
	// registry.addMapping("/greeting-javaconfig").allowedOrigins("http://localhost:4200");
	// }
	// };
	// }
	// @Bean
	// public CorsConfigurationSource corsConfigurationSource() {
	// 	CorsConfiguration apiCorsConfiguration = new CorsConfiguration();
	// 	apiCorsConfiguration.setAllowCredentials(true);
	// 	apiCorsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
	// 	apiCorsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
	// 	apiCorsConfiguration.setAllowedMethods(Collections.singletonList("*"));

	// 	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	// 	source.registerCorsConfiguration("/**", apiCorsConfiguration);
	// 	return source;
	// }
}
