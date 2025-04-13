package com.batch.bos;

import com.batch.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResBO {
	private Status status = Status.ERR;
	private String jwtToken;
	private int expiryMinuts;
	private String msg;
}
