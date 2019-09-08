package com.twitter.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.twitter.entity.User;

public interface UserService extends UserDetailsService {

	public void createUser(User user) throws Exception;

}
