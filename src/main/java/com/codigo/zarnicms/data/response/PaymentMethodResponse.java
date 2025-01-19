package com.codigo.zarnicms.data.response;

import java.io.Serializable;
import java.math.BigDecimal;


import lombok.Data;

@Data
public class PaymentMethodResponse implements Serializable {
	private static final long serialVersionUID = 6931915909017314676L;
	private long id;
	private String name;
	private BigDecimal discount;
}
