package com.codigo.zarnicms.data.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class JwtResponse implements Serializable {
	private static final long serialVersionUID = -5332893043019442952L;
	private final String jwttoken;
}
