package com.codigo.zarnicms.data.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class JwtRequest implements Serializable {
	private static final long serialVersionUID = -1836317618389529778L;
	private String username;
	private String password;
}
