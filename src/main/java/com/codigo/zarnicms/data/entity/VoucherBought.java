package com.codigo.zarnicms.data.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class VoucherBought implements Serializable {
	private static final long serialVersionUID = -7797004376972135213L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "voucherId")
	private Voucher voucher;
	
	@OneToOne
	@JoinColumn(name = "paymentMethodId")
	private PaymentMethod paymentMethod;
	
	private int quantity;
	
	private BigDecimal dueAmount;
	boolean paid = false;
	
	@Enumerated(EnumType.STRING)
	private VoucherType voucherType;
	
	private String name;
	private String phoneNumber;
	boolean active = true;
}
