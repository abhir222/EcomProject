package com.techband.assesment_task.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techband.assesment_task.entities.Product;
import com.techband.assesment_task.service.ProductService;

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
}
