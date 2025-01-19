package com.codigo.zarnicms.data.response;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class PurchaseHistoryPromoResponse implements Serializable {
	private static final long serialVersionUID = -3462114972260512870L;

	private Long id;
	
	@Column(unique=true)
	private String promoCode;
	
	@Lob
	byte[] qrImage;
	
	boolean used;
	
}
