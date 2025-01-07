package com.techband.assesment_task.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techband.assesment_task.entities.Product;
import com.techband.assesment_task.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}
	
	public List<Product> getProduct(){
		return productRepository.findAll();
	}
	
	public Optional<Product> findProductById(Long productId) {
		return productRepository.findById(productId);
	}
	
}
