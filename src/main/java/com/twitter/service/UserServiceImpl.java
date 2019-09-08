package com.twitter.service;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.twitter.dao.UserDao;
import com.twitter.entity.User;
import com.twitter.exception.TwitterException;

@Service
public class UserServiceImpl implements UserService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public UserDao userDao;

	@Override
	public void createUser(User user) throws Exception {
		User newUser = userDao.findByEmail(user.getEmail());
		Optional<User> newUserObj = Optional.ofNullable(newUser);
		if (newUserObj.isPresent()) {
			throw new TwitterException("User is already in use with email address " + newUserObj.get().getEmail());
		} else {
			userDao.save(user);
			logger.info("User is created succuessfully");
		}
	}

	@SuppressWarnings("null")
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userDao.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email address: " + user.getEmail());
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				new ArrayList<>());
	}

}