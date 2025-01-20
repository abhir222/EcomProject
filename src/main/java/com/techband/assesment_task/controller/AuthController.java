package com.techband.assesment_task.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techband.assesment_task.configuration.JwtHelper;
import com.techband.assesment_task.dto.JwtRequest;
import com.techband.assesment_task.dto.JwtResponse;
import com.techband.assesment_task.dto.RefreshTokenRequest;
import com.techband.assesment_task.entities.RefreshToken;
import com.techband.assesment_task.entities.User;
import com.techband.assesment_task.exception.UserNotFoundException;
import com.techband.assesment_task.service.RefreshTokenService;
import com.techband.assesment_task.service.UserService;


@RestController
@RequestMapping("/api")
public class AuthController {
   
//	private static final String SECRET_KEY = "my-secret-key";
//    private static final long EXPIRATION_TIME = 900000; // 15 minutes
//    private static final long REFRESH_EXPIRATION_TIME = 86400000; // 24 hours
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtHelper helper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> registerUser(@RequestBody User userEntity){
		//check whether user is present or not 
		if(userService.findByEmail(userEntity.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body("Email is already in use!");
		}
		
		//hashing the password before saving
		String hashedPassword = passwordEncoder.encode(userEntity.getPassword());
		User user = new User();
		user.setName(userEntity.getName());
		user.setEmail(userEntity.getEmail());
		user.setPassword(hashedPassword);
		
		userService.saveUser(user);
		return ResponseEntity.ok("User registered successfully");
		
	}
	
	@GetMapping("/registered-user")
	public String getRegisteredUser(Principal principal) {
		return principal.getName();
	}
	
	@GetMapping("/users")
	public List<User> usersList(){
		return userService.getAllUsers();
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<?> removeUser(@PathVariable("userId") Long id){
			try {
				userService.deleteUserById(id);
				return ResponseEntity.ok("User data removed succsefully!");
			} catch(UserNotFoundException ex) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
			} catch(Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the user");
			}
		
	}
	
	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
		
		this.doAuthentication(request.getEmail(), request.getPassword());
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		String token = this.helper.generateToken(userDetails);
		
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
		
		JwtResponse response = JwtResponse.builder()
				.jwtToken(token)
				.refreshToken(refreshToken.getRefreshToken())
				.username(userDetails.getUsername()).build();
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	private void doAuthentication(String email, String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
		try {
			authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid username or password!!");
		}
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public String exceptionHandler() {
		return "Invalid credentials!!";
	}
	
	@PostMapping("/refresh")
	public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
		
		RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
		
		User user = refreshToken.getUser();
		
		String token = this.helper.generateToken(user);
		
		return JwtResponse.builder().refreshToken(refreshToken.getRefreshToken())
				.jwtToken(token)
				.username(user.getEmail())
				.build();
	}

//	@PostMapping("/signin")
//	public Map<String, String> signIn(@RequestBody UserSignIn signIn){
//		
//		User user = userService.findByEmail(signIn.getEmail())
//			.orElseThrow(()-> new RuntimeException("Invalid Credentials!!"));
//		
//		if(!passwordEncoder.matches(signIn.getPassword(), user.getPassword())) {
//			throw new RuntimeException("Invalid Credentials!!");
//		}
//		
//		String accessToken = generateToken(user.getEmail(), EXPIRATION_TIME);
//        String refreshToken = generateToken(user.getEmail(), REFRESH_EXPIRATION_TIME);
//		
//		return Map.of("access_token", accessToken, "refresh_token", refreshToken);
//		
//	}
//	
//	private String generateToken(String subject, long expirationTime) {
//		return Jwts.builder()
//				.setSubject(subject)
//				.setIssuedAt(new Date())
//				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//				.compact();
//	}
	
//	@PostMapping("/refresh")
//	public Map<String, String> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
//		try {
//			String email = Jwts.parser()
//				.setSigningKey(SECRET_KEY)
//				.parseClaimsJws(refreshTokenRequest.getRefreshToken())
//				.getBody()
//				.getSubject();
//			
//			String newAccessToken = generateToken(email, EXPIRATION_TIME);
//			
//			return Map.of("access_token", newAccessToken);
//			
//		} catch (Exception e) {
//			throw new RuntimeException("Invalid refresh token!!");
//		}
//	}
	
}
