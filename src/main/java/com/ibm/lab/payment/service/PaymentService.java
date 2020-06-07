package com.ibm.lab.payment.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.lab.payment.adapter.PaymentChannelAdapter;
import com.ibm.lab.payment.dto.PaymentDto;
import com.ibm.lab.payment.model.Payment;
import com.ibm.lab.payment.model.ReservedPayment;
import com.ibm.lab.payment.model.Status;
import com.ibm.lab.payment.repository.PaymentRepository;


@Service
public class PaymentService {
	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
	
	@Autowired
	private PaymentChannelAdapter paymentChannelAdapter;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Value("${academy.payment-max-count}")
	private Long paymentMaxCount;
	/**
	 * 재고량 조회 
	 * @return
	 */
	public List<Payment> findAll() {
		return paymentRepository.findAll(paymentMaxCount);
	}
	
	/**
	 * 재고량 수정 
	 * @param stockDto
	 */
	public void savePayment(PaymentDto paymentDto) {
		Payment payment = paymentRepository.findByOrderId(paymentDto.getOrderId());
		payment.setAmt(paymentDto.getAmt());
		
		paymentRepository.savePayment(payment);
	}
	
    
    @Transactional
    public void confirmPayment(Long id) {
        ReservedPayment reservedPayment = paymentRepository.findByReservedPaymentId(id);

        if(reservedPayment == null) {
            throw new IllegalArgumentException("Not found");
        }

        reservedPayment.validate();

        // ReservedStock 상태를 Confirm 으로 변경
        reservedPayment.setStatus(Status.CONFIRMED.toString());
        
        logger.info("reservedPayment:{}", reservedPayment);
        paymentRepository.saveReservedPayment(reservedPayment);

        // Messaging Queue 로 전송
        paymentChannelAdapter.publish(reservedPayment.getResources());
        logger.info("Confirm Stock :" + id);
    }

    @Transactional
    public void cancelPayment(Long id) {
    	ReservedPayment reservedStock = paymentRepository.findByReservedPaymentId(id);
    	
        reservedStock.setStatus(Status.CANCEL.toString());
        paymentRepository.saveReservedPayment(reservedStock);

        logger.info("Cancel Stock :" + id);
    }

    @Transactional
    public void payOrder(final String orderId, final Long amount) {
        // Payment Gateway 연동 등등..
        // ...
        final Payment payment = new Payment(orderId, amount);
        
        logger.info("Paid payment..{}", payment);
        
        paymentRepository.savePayment(payment);

        
    }

    public ReservedPayment reservePayment(PaymentDto paymentDto) {
        // 유효성 검사
    	paymentDto.validate();

        ReservedPayment reservedPayment = new ReservedPayment(paymentDto);
        Long reservedPaymentId = paymentRepository.genSeqReservedPayment();
        reservedPayment.setId(reservedPaymentId);
        
        paymentRepository.saveReservedPayment(reservedPayment);

        logger.info("Reserved Payment :" + reservedPayment.getId());
        return reservedPayment;
    }


    public boolean isAlreadyProcessedOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId);

        if(payment == null) {
            return false;
        } else {
            return true;
        }
    }
}
