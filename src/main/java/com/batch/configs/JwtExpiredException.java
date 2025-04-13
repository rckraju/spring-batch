package com.batch.configs;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtExpiredException extends RuntimeException {
	
	private String message;

	public JwtExpiredException(String msg) {
		super(msg);
        this.message = msg;
	}

}
