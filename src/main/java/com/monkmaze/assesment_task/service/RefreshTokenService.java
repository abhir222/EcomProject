package com.monkmaze.assesment_task.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monkmaze.assesment_task.entities.RefreshToken;
import com.monkmaze.assesment_task.entities.User;
import com.monkmaze.assesment_task.repository.RefreshTokenRepository;
import com.monkmaze.assesment_task.repository.UserRepository;

@Service
public class RefreshTokenService {

	private final long refreshTokenValidity = 2*60*1000;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public RefreshToken createRefreshToken(String userName){
		
		User user = userRepository.findByEmail(userName).get();
		RefreshToken refreshToken1 = user.getRefreshToken();
		
		if(refreshToken1 == null) {
			refreshToken1 = RefreshToken.builder()
			.refreshToken(UUID.randomUUID().toString())
			.expiry(Instant.now().plusMillis(refreshTokenValidity))
			.user(user)
			.build();
		} else {
			refreshToken1.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
		}
		
		user.setRefreshToken(refreshToken1);
		
		//Save to database
		refreshTokenRepository.save(refreshToken1);
		
		return refreshToken1;
	}
	
	public RefreshToken verifyRefreshToken(String refreshToken) {
		RefreshToken refreshTokenOb = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException("Given token does not exist in DB!!"));
		if(refreshTokenOb.getExpiry().compareTo(Instant.now())<0) {
			refreshTokenRepository.delete(refreshTokenOb);
			throw new RuntimeException("Refresh Token Expired!!");
		}
		return refreshTokenOb;
	}
}
