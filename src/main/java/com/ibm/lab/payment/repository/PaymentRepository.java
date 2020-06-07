package com.ibm.lab.payment.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibm.lab.payment.model.Payment;
import com.ibm.lab.payment.model.ReservedPayment;

@Repository
public class PaymentRepository {
	private static final Logger logger = LoggerFactory.getLogger(PaymentRepository.class);
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	/**
	 * 등록 
	 * @param paymentDto
	 * @return
	 */
	public int savePayment(Payment payment) {
		int record = sqlSessionTemplate.insert("payment.savePayment", payment);
		logger.info("payment rc:{}", record);
		
		return record;
	}
	
	/**
	 * payment 10 건 조회 
	 * @return
	 */
	public List<Payment> findAll(Long limit) {
		List<Payment> payments = sqlSessionTemplate.selectList("payment.findAll", limit);
		logger.info("payment list:{}", payments);
		
		return payments;
	}
	
	public Payment findByOrderId(String id) {
		Payment payment = sqlSessionTemplate.selectOne("payment.findByOrderId", id);
		
		return payment;
	}
	
	public ReservedPayment findByReservedPaymentId(Long id) {
		ReservedPayment reservedPayment = sqlSessionTemplate.selectOne("payment.findByReservedPaymentId", id);
		
		return reservedPayment;
	}
	
	public Long getSeqReservedPayment() {
		Long seq = sqlSessionTemplate.selectOne("payment.getSeqReservedPayment");
		
		return seq;
	}
	
	public Long genSeqReservedPayment() {
		Long seq = sqlSessionTemplate.selectOne("payment.genSeqReservedPayment");
		
		return seq;
	}
	
	public ReservedPayment findByPayment(Long id) {
		ReservedPayment payment = sqlSessionTemplate.selectOne("payment.findByPayment", id);
		
		return payment;
	}
	
	public int saveReservedPayment(ReservedPayment reservedStock) {
		int record = sqlSessionTemplate.insert("payment.saveReservedPayment", reservedStock);
		
		return record;
	}
}
