package com.codigo.zarnicms.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codigo.zarnicms.common.Currency;
import com.codigo.zarnicms.common.CustomException;
import com.codigo.zarnicms.data.entity.PaymentMethod;
import com.codigo.zarnicms.data.entity.Voucher;
import com.codigo.zarnicms.data.entity.VoucherBought;
import com.codigo.zarnicms.data.entity.VoucherPromoCode;
import com.codigo.zarnicms.data.entity.VoucherType;
import com.codigo.zarnicms.data.repository.PaymentMethodRepository;
import com.codigo.zarnicms.data.repository.VoucherBoughtRepository;
import com.codigo.zarnicms.data.repository.VoucherPromoCodeRepository;
import com.codigo.zarnicms.data.repository.VoucherRepository;
import com.codigo.zarnicms.data.request.VoucherCheckoutChargeRequest;
import com.codigo.zarnicms.data.request.VoucherCheckoutRequest;
import com.codigo.zarnicms.data.request.VoucherCreateRequest;
import com.codigo.zarnicms.data.request.VoucherEditRequest;
import com.codigo.zarnicms.data.response.PaymentMethodResponse;
import com.codigo.zarnicms.data.response.PurchaseHistoryPromoResponse;
import com.codigo.zarnicms.data.response.PurchaseHistoryResponse;
import com.codigo.zarnicms.data.response.VoucherCheckoutChargeResponse;
import com.codigo.zarnicms.data.response.VoucherCheckoutResponse;
import com.codigo.zarnicms.data.response.VoucherDetailResponse;
import com.codigo.zarnicms.data.response.VoucherResponse;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class VoucherService {
	@Autowired
	private PaymentMethodRepository paymentMethodRepository;
	
	@Autowired
	private VoucherRepository voucherRepository;
	
	@Autowired
	private VoucherBoughtRepository voucherBoughtRepository;
	
	@Autowired
	private VoucherPromoCodeRepository voucherPromoCodeRepository;
	
	@Autowired
	private PromoCodeService promoCodeService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${stripe.public-key}")
	private String stripePublicKey;
	
	@Value("${stripe.secret-key}")
	private String stripeSecretKey;
	
	public VoucherResponse createVoucher(VoucherCreateRequest request) throws Exception {
		Voucher voucher = modelMapper.map(request, Voucher.class);
		
		voucher = voucherRepository.save(voucher);
		
		VoucherResponse response = modelMapper.map(voucher, VoucherResponse.class);
		return response;
	}
	
	@Transactional
	public VoucherResponse editVoucher(VoucherEditRequest request) throws Exception {
		
		Voucher existingVoucher = voucherRepository.findById(request.getId()).orElseThrow();
		Long count = voucherBoughtRepository.countByVoucher(existingVoucher);
		boolean bought = count != null && count > 0;
		if(bought) {
			// should save new, should not edit used voucher
			// TODO FIXME to test after voucher is bought
			Voucher voucher = modelMapper.map(request, Voucher.class);
			voucher.setId(null); // to ensure new record is saved
			voucher = voucherRepository.save(voucher);
			
			existingVoucher.setActive(false); // mark old voucher as inactive
			voucherRepository.save(existingVoucher);
			
			existingVoucher = voucher;
		} else {
			existingVoucher = modelMapper.map(request, Voucher.class);
			existingVoucher = voucherRepository.save(existingVoucher);
		}
		
		VoucherResponse response = modelMapper.map(existingVoucher, VoucherResponse.class);
		return response;
	}
	
	@Transactional
	public void inactiveVoucher(long voucherId) throws Exception {
		Voucher voucher =voucherRepository.findById(voucherId).orElseThrow();
		if(voucher.getId() == null) {
			throw new CustomException(HttpStatus.CONFLICT, "Voucher has not created.");
		}
		voucherRepository.setVoucherInactive(voucherId);
	}
	
	public List<VoucherResponse> getallVoucher() throws Exception {
		List<Voucher> list = voucherRepository.findAll();
		List<VoucherResponse> response = new ArrayList<>();
		
		for (Voucher voucher : list) {
			VoucherResponse res = modelMapper.map(voucher, VoucherResponse.class);
			response.add(res);
		}
		return response;
	}
	
	public VoucherDetailResponse getDetailVoucher(Long voucherId) throws Exception {
		Voucher voucher = voucherRepository.findById(voucherId).orElseThrow();
		VoucherDetailResponse response  = modelMapper.map(voucher, VoucherDetailResponse.class);
		return response;
	}
	
	public List<PaymentMethodResponse> getPaymentMethods() throws Exception {
		List<PaymentMethod> list = paymentMethodRepository.findAll();
		List<PaymentMethodResponse> response = new ArrayList<>();
		
		for (PaymentMethod method : list) {
			PaymentMethodResponse res = modelMapper.map(method, PaymentMethodResponse.class);
			response.add(res);
		}
		return response;
	}
	
	//CheckOut
	@Transactional
	public VoucherCheckoutResponse checkOutVoucher(VoucherCheckoutRequest request) throws Exception {
		Stripe.apiKey = stripeSecretKey;
		
		VoucherBought voucherBought = modelMapper.map(request, VoucherBought.class);
		
		Voucher voucher = voucherRepository.findById(request.getVoucherId()).orElseThrow();
		if(
			(VoucherType.OnlyMeUsage.equals(request.getVoucherType()) && 
					request.getQuantity() > voucher.getBuyLimit())
			||
			(VoucherType.GiftToOthers.equals(request.getVoucherType()) && 
					request.getQuantity() > voucher.getGiftLimit())
		) {
			throw new Exception("Quantity is above limit.");
		}
		
		BigDecimal dueAmount = voucher.getAmount() // 100
				.multiply(BigDecimal.valueOf(request.getQuantity())); // * 10
		
		// Retrieve the PaymentMethod object
		com.stripe.model.PaymentMethod paymentMethod = com.stripe.model.PaymentMethod.retrieve(request.getStripePaymentMethodId());
		
		// Get the card brand, and determine discount
		String cardBrand = paymentMethod.getCard().getBrand();
		List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
		PaymentMethod usedPaymentMethod = null;
		for (PaymentMethod p : paymentMethods) {
			if(cardBrand.equalsIgnoreCase(p.getName())) {
				usedPaymentMethod = p;
				dueAmount = dueAmount.subtract( // dueAmount - discount of dueAmount
						dueAmount.multiply(p.getDiscount().divide(BigDecimal.valueOf(100)))); 
				break;
			}
		}
		
		voucherBought.setVoucher(voucher);
		voucherBought.setDueAmount(dueAmount);
		voucherBought.setPaid(false);
		voucherBought.setPaymentMethod(usedPaymentMethod);
		voucherBought = voucherBoughtRepository.save(voucherBought);
		
		VoucherCheckoutResponse response = modelMapper.map(voucherBought, VoucherCheckoutResponse.class);
		response.setDueAmount(dueAmount);
		response.setCurrency(Currency.USD);
		return response;
	}
	
	//Payment
	@Transactional
	public VoucherCheckoutChargeResponse payment(VoucherCheckoutChargeRequest request) throws Exception {
		VoucherBought voucherBought = voucherBoughtRepository.findById(request.getVoucherBoughtId()).orElseThrow();
		
		if(voucherBought.isPaid()) {
			throw new CustomException(HttpStatus.CONFLICT, "This checked out voucher is already bought, please checkout again.");
		}
		
		try {
			Stripe.apiKey = stripeSecretKey;
			
			String token = request.getStripeToken();
			long chargeAmount = voucherBought.getDueAmount().longValue() * 100;  // amount is in cents
			
			ChargeCreateParams params =
			  ChargeCreateParams.builder()
				.setAmount(chargeAmount)
				.setCurrency("usd")
				.setDescription(String.format("Payment for %s of %s", voucherBought.getQuantity(), voucherBought.getVoucher().getTitle()))
				.setSource(token)
				.build();
			Charge charge = Charge.create(params);
			
			String cardBrand = charge.getPaymentMethodDetails().getCard().getBrand();
			List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
			PaymentMethod usedPaymentMethod = null;
			for (PaymentMethod p : paymentMethods) {
				if(cardBrand.equalsIgnoreCase(p.getName())) {
					usedPaymentMethod = p;
				}
			}
			
			if(!usedPaymentMethod.equals(voucherBought.getPaymentMethod())) {
				throw new CustomException(HttpStatus.UNAUTHORIZED,"Paying with different payment method from checkout");
			}
			
			voucherBought.setPaid(true);
			voucherBought = voucherBoughtRepository.save(voucherBought);
			
			promoCodeService.generatePromoCodes(voucherBought);
			
			VoucherCheckoutChargeResponse response = modelMapper.map(voucherBought, VoucherCheckoutChargeResponse.class);
			return response;
		} catch (Exception e) {
			log.error("Payment unsuccsssful from stripe ", e);
			throw new CustomException(HttpStatus.PAYMENT_REQUIRED,"Payment unsuccesssful.");
		}
	}
	
	public VoucherDetailResponse verifyPromoCode(String promoCode) throws Exception {
		VoucherPromoCode voucherPromoCode = voucherPromoCodeRepository.findByPromoCode(promoCode);
		if(voucherPromoCode.isUsed()) {
			throw new CustomException(HttpStatus.NOT_ACCEPTABLE,"PromoCode is already used.");
		}
		if(voucherPromoCode.getVoucherBought().getVoucher().getExpiryDate()
				.isBefore(LocalDateTime.now(ZoneId.of("Asia/Singapore")))) {
			throw new CustomException(HttpStatus.GONE,"PromoCode is already expired.");
		}
		
		voucherPromoCode.setUsed(true);
		voucherPromoCodeRepository.save(voucherPromoCode);
		
		VoucherDetailResponse response  = modelMapper.map(voucherPromoCode.getVoucherBought().getVoucher(), VoucherDetailResponse.class);
		return response;
	}
	
	public List<PurchaseHistoryResponse> purchaseHistory(String phoneNumber) throws Exception {
		List<VoucherPromoCode> promoList = voucherPromoCodeRepository.findByVoucherBoughtPhoneNumber(phoneNumber);
		List<PurchaseHistoryResponse> response = new ArrayList<>();
		
		Map<VoucherBought, List<VoucherPromoCode>> groupedByVoucherBought = promoList.stream()
				.collect(Collectors.groupingBy(VoucherPromoCode::getVoucherBought));
		
		for (VoucherBought v : groupedByVoucherBought.keySet()) {
			List<VoucherPromoCode> voucherPromoCodes =  groupedByVoucherBought.get(v);
			PurchaseHistoryResponse res = modelMapper.map(v, PurchaseHistoryResponse.class);
			List<PurchaseHistoryPromoResponse> promoCodes = 
					modelMapper.map(voucherPromoCodes, new TypeToken<List<PurchaseHistoryPromoResponse>>() {}.getType());
			res.setPromoCodes(promoCodes);
			response.add(res);
		}
		return response;
	}
}
