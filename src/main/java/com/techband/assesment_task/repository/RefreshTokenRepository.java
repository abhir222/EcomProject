package com.techband.assesment_task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techband.assesment_task.entities.RefreshToken;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

	Optional<RefreshToken> findByRefreshToken(String token);
}
