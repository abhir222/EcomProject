package com.techband.assesment_task.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techband.assesment_task.entities.User;
import com.techband.assesment_task.exception.UserNotFoundException;
import com.techband.assesment_task.repository.RefreshTokenRepository;
import com.techband.assesment_task.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Transactional
	public void deleteUserById(Long id) throws UserNotFoundException {
		//delete refresh token associated with user
		refreshTokenRepository.deleteByUserId(id);
		
		if(!userRepository.existsById(id)) {
			throw new UserNotFoundException("User with ID "+ id + " not found");
		}
		
		//delete the user
		userRepository.deleteById(id);
	}

}
