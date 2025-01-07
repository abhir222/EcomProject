package com.techband.assesment_task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techband.assesment_task.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findById(Long productId);
	
}
