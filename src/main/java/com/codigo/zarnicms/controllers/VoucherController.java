package com.codigo.zarnicms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codigo.zarnicms.data.request.VoucherCreateRequest;
import com.codigo.zarnicms.data.request.VoucherEditRequest;
import com.codigo.zarnicms.data.response.VoucherResponse;
import com.codigo.zarnicms.service.VoucherService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("voucher-cms")
public class VoucherController {
	@Autowired
	VoucherService voucherService;
	
	@Autowired
	ObjectMapper mapper;
	
	@PostMapping(value = "/createVoucher")
	public ResponseEntity<?> createVoucher(@RequestBody VoucherCreateRequest request) throws Exception {
		VoucherResponse response = voucherService.createVoucher(request);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "/editVoucher")
	public ResponseEntity<?> editVoucher(@RequestBody VoucherEditRequest request) throws Exception {
		VoucherResponse response = voucherService.editVoucher(request);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "/inactiveVoucher")
	public ResponseEntity<?> inactiveVoucher(@RequestParam long voucherId) throws Exception {
		voucherService.inactiveVoucher(voucherId);
		return ResponseEntity.ok("Deactivated.");
	}
}