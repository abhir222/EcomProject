package com.techband.assesment_task.productHandler;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.techband.assesment_task.entities.Product;
import com.techband.assesment_task.service.ProductService;

@Controller
public class ProductPriceWebSocketHandler extends TextWebSocketHandler{

	@Autowired
	private ProductService productService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPriceWebSocketHandler.class); 
	
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String productId = session.getUri().getPath().split("/")[3];
		Long id = Long.parseLong(productId);
		//steps to save the price in database
		//fetch the product from the database
//		Product product = this.productService.findProductById(Long.parseLong(productId))
//				.orElseThrow(() -> new RuntimeException("Product Not Found!!"));
		
		Product product = productService.findProductById(id).orElseThrow(() -> new RuntimeException("Product not found!!"));
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(0);
		executor.scheduleAtFixedRate(() -> {
			try {
				//simulated price update
				double simulatedPrice = Math.random()*100; 
				
				System.out.println(product);
				
				//update the price
				product.setPrice(simulatedPrice);
				//save the updated price in DB
				productService.saveProduct(product);
				
				LOGGER.info("Product ID: "+ productId + " Price updated to: "+ simulatedPrice);
				
				session.sendMessage(new TextMessage("{\"product_id\": " + productId + ", \"real_time_price\": " + simulatedPrice + "}"));
				
			} catch (Exception e) {
				executor.shutdown();
			}
		}, 0, 5, TimeUnit.SECONDS);
	}
	
}
