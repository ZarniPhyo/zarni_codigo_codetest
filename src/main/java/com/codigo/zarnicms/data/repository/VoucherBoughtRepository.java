package com.codigo.zarnicms.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codigo.zarnicms.data.entity.VoucherBought;
import com.codigo.zarnicms.data.entity.Voucher;

public interface VoucherBoughtRepository extends JpaRepository<VoucherBought, Long>{
	Long countByVoucher(Voucher voucher);
	
	
}
