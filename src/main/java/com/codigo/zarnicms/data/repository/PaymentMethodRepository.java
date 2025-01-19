package com.codigo.zarnicms.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codigo.zarnicms.data.entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}