package com.techband.assesment_task.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.techband.assesment_task.productHandler.ProductPriceWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new ProductPriceWebSocketHandler(), "/ws/product-price/{productId}")
				.setAllowedOrigins("*"); //allowed cross-origin request	
	}

}
