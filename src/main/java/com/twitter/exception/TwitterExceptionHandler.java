package com.twitter.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TwitterExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
		ExceptionResponse eresp = new ExceptionResponse("Server Error", ex.getLocalizedMessage());
		return new ResponseEntity<ExceptionResponse>(eresp, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(TwitterException.class)
	public final ResponseEntity<Object> handleUserAreadyExistsException(TwitterException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse error = new ExceptionResponse("Validation Failed",
				"validation Failed with property " + ex.getBindingResult().getFieldError().getField().toString());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}