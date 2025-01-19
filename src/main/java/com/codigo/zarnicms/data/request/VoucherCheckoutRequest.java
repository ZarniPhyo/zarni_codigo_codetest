package com.codigo.zarnicms.data.request;

import java.io.Serializable;

import com.codigo.zarnicms.data.entity.VoucherType;

import lombok.Data;

@Data
public class VoucherCheckoutRequest implements Serializable {
	private static final long serialVersionUID = -3480643229153027794L;
	
	private long voucherId;
	private int quantity;
	
	private VoucherType voucherType;
	private String name;
	private String phoneNumber;
	
	private String stripePaymentMethodId;
}
