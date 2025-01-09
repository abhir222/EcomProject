package com.techband.assesment_task.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techband.assesment_task.entities.Product;
import com.techband.assesment_task.service.ProductService;

import lombok.var;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		 Product createdProduct = productService.saveProduct(product);
		 return ResponseEntity.ok(createdProduct);
	}
	
	@GetMapping
	public List<Product> productsList(){
		return productService.getProduct();
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<Product> getProductById(@PathVariable("productId") Long productId) {
		Product product = productService.findProductById(productId)
				.orElseThrow(() -> new RuntimeException("requested product is not in DB!!"));
		return ResponseEntity.ok().body(product);
	}
	
	@PostMapping("/buy/{productId}")
	public Map<String, Object> buyProduct(@PathVariable("productId") long productId){
		var product = productService.findProductById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found!!"));
		return Map.of(
					"message", "Purchase Successful",
					"product_id", product.getProductId()
				);
	}
}
