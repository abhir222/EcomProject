package com.monkmaze.assesment_task.productHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller
public class ProductPriceHandlerWenS extends TextWebSocketHandler{

	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String productId = session.getUri().getPath().split("/")[3];
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(0);
		executor.scheduleAtFixedRate(() -> {
			try {
				double simulatedPrice = Math.random()*100; //simulated price update
				session.sendMessage(new TextMessage("{\"product_id\": " + productId + ", \"real_time_price\": " + simulatedPrice + "}"));
				
			} catch (Exception e) {
				executor.shutdown();
			}
		}, 0, 5, TimeUnit.SECONDS);
	}
	
}
