package com.codigo.zarnicms.data.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoucherCreateRequest implements Serializable {
	private static final long serialVersionUID = -6704358292954819735L;

	@NotBlank(message =  "title is required")
	private String title;
	@NotBlank(message =  "description is required")
	private String description;
	
	@NotNull(message =  "expiryDate is required")
	private LocalDateTime expiryDate;
	
	@NotNull
	@Min(value = (long) 0.01, message = "Amount must be greater than zero")
	private BigDecimal Amount;
	
	@Min(value = 1, message = "buyLimit must be greater than zero")
	private int buyLimit;
	
	@Min(value = 1, message = "giftLimit must be greater than zero")
	private int giftLimit;
	
	boolean active;
}
