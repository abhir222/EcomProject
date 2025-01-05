package com.monkmaze.assesment_task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monkmaze.assesment_task.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	
}
