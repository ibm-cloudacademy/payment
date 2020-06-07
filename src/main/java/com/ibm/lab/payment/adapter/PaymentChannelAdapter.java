package com.ibm.lab.payment.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.ibm.lab.payment.dto.PaymentDto;
import com.ibm.lab.payment.service.PaymentService;

@Component
public class PaymentChannelAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentChannelAdapter.class);
	
	@Autowired
    private KafkaTemplate<Long, String> kafkaTemplate;
	
	@Value("${kafka.payment.topic}")
	private String TOPIC;
	
    private PaymentService paymentService;

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
	
    public void publish(final String message) {
        this.kafkaTemplate.send(TOPIC, message);
    }
    
    @KafkaListener(topics = "${kafka.payment.topic}")
    public void subscribe(String message, Acknowledgment ack) {
        logger.info(String.format("Message Received : %s", message));

        try {
            PaymentDto paymentRequest = PaymentDto.deserializeJSON(message);

            // 이미 처리된 주문인지 확인
            if(paymentService.isAlreadyProcessedOrderId(paymentRequest.getOrderId())) {
                logger.info(String.format("AlreadyProcessedOrderId : [%s]", paymentRequest.getOrderId()));
            } else {
                paymentService.payOrder(paymentRequest.getOrderId(), paymentRequest.getAmt());
            }

            // Kafka Offset Manual Commit
            ack.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
