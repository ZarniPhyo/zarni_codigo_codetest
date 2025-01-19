package com.codigo.zarnicms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codigo.zarnicms.data.request.VoucherCheckoutChargeRequest;
import com.codigo.zarnicms.data.request.VoucherCheckoutRequest;
import com.codigo.zarnicms.data.response.PaymentMethodResponse;
import com.codigo.zarnicms.data.response.PurchaseHistoryResponse;
import com.codigo.zarnicms.data.response.VoucherCheckoutChargeResponse;
import com.codigo.zarnicms.data.response.VoucherCheckoutResponse;
import com.codigo.zarnicms.data.response.VoucherDetailResponse;
import com.codigo.zarnicms.data.response.VoucherResponse;
import com.codigo.zarnicms.service.VoucherService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("voucher-estore")
public class StoreController {
	@Autowired
	VoucherService voucherService;
	
	@Autowired
	ObjectMapper mapper;
	
	@GetMapping(value = "/returnVoucherList")
	public ResponseEntity<?> getAllVoucher() throws Exception {
		List<VoucherResponse> response = voucherService.getallVoucher();
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/returnVoucherDetail")
	public ResponseEntity<?> getVoucherDetail(long voucherId) throws Exception {
		VoucherDetailResponse response = voucherService.getDetailVoucher(voucherId);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/showPaymentMethodList")
	public ResponseEntity<?> getPaymentMethods() throws Exception {
		List<PaymentMethodResponse> response = voucherService.getPaymentMethods();
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "/checkOutVoucher")
	public ResponseEntity<?> checkOutVoucher(@RequestBody VoucherCheckoutRequest request) throws Exception {
		VoucherCheckoutResponse response = voucherService.checkOutVoucher(request);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "/makePayment")
	public ResponseEntity<?> makePayment(@RequestBody VoucherCheckoutChargeRequest request) throws Exception {
		VoucherCheckoutChargeResponse response = voucherService.payment(request);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "/verifyPromoCode")
	public ResponseEntity<?> verifyPromoCode(@RequestParam String promoCode) throws Exception {
		VoucherDetailResponse response = voucherService.verifyPromoCode(promoCode);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "/purchaseHistory")
	public ResponseEntity<?> purchaseHistory(@RequestParam String phoneNumber) throws Exception {
		List<PurchaseHistoryResponse> response = voucherService.purchaseHistory(phoneNumber);
		return ResponseEntity.ok(response);
	}
}