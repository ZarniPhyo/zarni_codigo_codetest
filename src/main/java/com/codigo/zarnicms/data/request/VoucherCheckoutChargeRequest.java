package com.codigo.zarnicms.data.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class VoucherCheckoutChargeRequest implements Serializable {
	private static final long serialVersionUID = 5145291947539192810L;
	private long voucherBoughtId;
	private long paymentMethodId;
    private String stripeToken;
}