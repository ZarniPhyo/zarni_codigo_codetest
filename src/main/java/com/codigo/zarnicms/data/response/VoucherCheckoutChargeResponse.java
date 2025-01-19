package com.codigo.zarnicms.data.response;

import java.io.Serializable;
import java.math.BigDecimal;

import com.codigo.zarnicms.common.Currency;
import com.codigo.zarnicms.data.entity.VoucherType;

import lombok.Data;

@Data
public class VoucherCheckoutChargeResponse implements Serializable {
	private static final long serialVersionUID = -3790005179797072593L;
	private long id;
	
	private VoucherResponse voucher;
	private PaymentMethodResponse paymentMethod;
	private int quantity;
	private VoucherType voucherType;
	private String name;
	private String phoneNumber;
	
	private BigDecimal dueAmount;
	private Currency currency;
}