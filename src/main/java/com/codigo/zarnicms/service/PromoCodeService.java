package com.codigo.zarnicms.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.codigo.zarnicms.data.entity.VoucherBought;
import com.codigo.zarnicms.data.entity.VoucherPromoCode;
import com.codigo.zarnicms.data.repository.VoucherPromoCodeRepository;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;


@Service
@Slf4j
public class PromoCodeService {
	@Autowired
	private VoucherPromoCodeRepository voucherPromoCodeRepository;
	
	
	@Async
	public void generatePromoCodes(VoucherBought voucherBought) throws Exception {
		List<String> usedPromoCodes = new ArrayList<>();
		List<VoucherPromoCode> promoCodes = new ArrayList<>();
		for(int i = 1; i <= voucherBought.getQuantity(); i++) {
			String promoCode = getPromoCode(usedPromoCodes);
			Barcode barcode = BarcodeFactory.createCode128(promoCode);
			BufferedImage image = BarcodeImageHandler.getImage(barcode);
			byte[] bytes = toByteArray(image, "png");
			
			VoucherPromoCode newPromoCode = new VoucherPromoCode();
			newPromoCode.setVoucherBought(voucherBought);
			newPromoCode.setPromoCode(promoCode);
			newPromoCode.setQrImage(bytes);
			promoCodes.add(newPromoCode);
		}
		voucherPromoCodeRepository.saveAll(promoCodes);		
	}
	
	private String getPromoCode(List<String> usedPromoCodes) {
		String digits = "0123456789";
		String randomDigits = RandomStringUtils.random(6, 0, 0, true, true, digits.toCharArray(), new Random());
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String randomAlphabets = RandomStringUtils.random(5, 0, 0, true, true, characters.toCharArray(), new Random());
		String newPromoCode = randomDigits + randomAlphabets;
		if(usedPromoCodes.contains(newPromoCode)) {
			return getPromoCode(usedPromoCodes); // recursive
		}else {
			usedPromoCodes.add(newPromoCode);
		}
		return newPromoCode;
	}
	
	private static byte[] toByteArray(BufferedImage bi, String format)
		throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, format, baos);
		byte[] bytes = baos.toByteArray();
		return bytes;
	}
}
