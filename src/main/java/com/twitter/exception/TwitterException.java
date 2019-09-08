package com.twitter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TwitterException extends RuntimeException {

	public TwitterException(String message) {
		super(message);
	}

}
