package com.example.demo.exception;

public class AIModerationException extends RuntimeException {
	public AIModerationException(String message, Throwable cause) {
		super(message, cause);
	}
	public AIModerationException(String message) {
        super(message);
    }
}
