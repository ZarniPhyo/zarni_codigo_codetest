package com.codigo.zarnicms.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codigo.zarnicms.data.entity.VoucherPromoCode;

public interface VoucherPromoCodeRepository extends JpaRepository<VoucherPromoCode, Long>{
	VoucherPromoCode findByPromoCode(String promoCode);

	List<VoucherPromoCode> findByVoucherBoughtPhoneNumber(String phoneNumber);
}
