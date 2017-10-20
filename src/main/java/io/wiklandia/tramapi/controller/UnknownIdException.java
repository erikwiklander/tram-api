package io.wiklandia.tramapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnknownIdException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public UnknownIdException() {
		super();
	}

	public UnknownIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknownIdException(String message) {
		super(message);
	}

	public UnknownIdException(Throwable cause) {
		super(cause);
	}
}
