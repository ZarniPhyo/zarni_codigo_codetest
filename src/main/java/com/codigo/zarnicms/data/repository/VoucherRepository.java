package com.codigo.zarnicms.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codigo.zarnicms.data.entity.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
	// create -> save
	// edit -> save
	
	// inactive
	@Modifying
	@Query("update Voucher v set v.active = false where v.id = :id ")
	void setVoucherInactive(@Param("id") Long id);
}