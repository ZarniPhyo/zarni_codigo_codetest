package com.codigo.zarnicms.data.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Voucher implements Serializable {
	private static final long serialVersionUID = 261085371999938214L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message =  "title is required")
	private String title;
	
	@NotBlank(message =  "description is required")
	private String description;
	
	@NotNull(message =  "expiryDate is required")
	private LocalDateTime expiryDate;
	
	@NotNull
	@Min(value = (long) 0.01, message = "Amount must be greater than zero")
	private BigDecimal Amount;
	
	@Min(value = 1, message = "BuyLimit must be greater than zero")
	private int buyLimit;
	@Min(value = 1, message = "GiftLimit must be greater than zero")
	private int giftLimit;
	
	boolean active = true;
}
