package com.techband.assesment_task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techband.assesment_task.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
}
