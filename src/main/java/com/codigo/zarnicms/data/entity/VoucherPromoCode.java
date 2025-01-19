package com.codigo.zarnicms.data.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class VoucherPromoCode implements Serializable {
	private static final long serialVersionUID = 3137804202871027317L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "voucherBoughtId")
	private VoucherBought voucherBought;
	
	@Column(unique=true)
	private String promoCode;
	
	@Lob
	byte[] qrImage;
	
	boolean used = false;
}
