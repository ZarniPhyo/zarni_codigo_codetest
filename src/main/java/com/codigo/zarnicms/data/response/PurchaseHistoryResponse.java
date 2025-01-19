package com.codigo.zarnicms.data.response;

import java.io.Serializable;
import java.util.List;

import com.codigo.zarnicms.data.entity.VoucherType;

import lombok.Data;

@Data
public class PurchaseHistoryResponse implements Serializable {
	private static final long serialVersionUID = 2602411102892658566L;

	private Long id;
	
	private VoucherResponse voucher;
	
	private PaymentMethodResponse paymentMethod;
	
	private int quantity;
	
	private VoucherType voucherType;
	
	private String name;
	boolean active;

	List<PurchaseHistoryPromoResponse> promoCodes;
}
