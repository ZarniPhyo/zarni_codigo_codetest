package com.codigo.zarnicms.data.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class VoucherDetailResponse implements Serializable {
	private static final long serialVersionUID = -6489436146138492357L;

	private long id;
	
	private String title;
	private String description;
	private String expiryDate;
	
	private byte[] image;
	
	private BigDecimal Amount;
	
	private int buyLimit;
	private int giftLimit;

	boolean active;
}
