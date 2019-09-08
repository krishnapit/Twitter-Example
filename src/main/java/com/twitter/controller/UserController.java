package com.twitter.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.twitter.config.AuthenticationResponse;
import com.twitter.config.JwtTokenUtil;
import com.twitter.entity.User;
import com.twitter.exception.TwitterException;
import com.twitter.service.UserService;

/**
 *
 * @author krishna
 *
 */
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	/**
	 * This method is for create Twitter account
	 *
	 * @param user
	 * @return
	 */
	@PostMapping(path = "/signUp")
	public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
		Optional<User> newUser = Optional.ofNullable(user);
		if (newUser.isPresent()) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encryptedPassWord = passwordEncoder.encode(String.valueOf(user.getPassword()));
			user.setPassword(encryptedPassWord);
			try {
				userService.createUser(user);
			} catch (Exception ex) {
				if (ex instanceof TwitterException)
					return new ResponseEntity<>("User is already in use with email address " + user.getEmail(),
							HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Please provide valid user information", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(
				"Welcome " + user.getName() + ". your twitter account is created successfully ", HttpStatus.CREATED);
	}

	/**
	 *
	 * This method is for login into twitter account
	 *
	 * @param authenticationRequest *
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody User authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}

	private void authenticate(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (BadCredentialsException | InternalAuthenticationServiceException e) {
			throw new TwitterException("Invalid Credentials ");
		} catch (Exception e) {
			throw new TwitterException("Invalid Credentials ");
		}
	}
}
