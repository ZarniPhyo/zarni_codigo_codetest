package com.codigo.zarnicms.data.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class VoucherEditRequest implements Serializable {
	private static final long serialVersionUID = -6704358292954819735L;

	private long id;
	
	private String title;
	private String description;
	private LocalDateTime expiryDate;
	
	private BigDecimal Amount;
	
	private int buyLimit;
	private int giftLimit;
	
	boolean active;
}
