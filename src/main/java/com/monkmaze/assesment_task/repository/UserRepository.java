package com.monkmaze.assesment_task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monkmaze.assesment_task.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
}
