package com.techband.assesment_task.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.techband.assesment_task.productHandler.ProductPriceWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
	
	@Autowired
	private ProductPriceWebSocketHandler productPriceWebSocketHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(productPriceWebSocketHandler, "/ws/product-price/{productId}")
				.setAllowedOrigins("*"); //allowed cross-origin request	
	}

}
