package com.ibm.lab.payment.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.lab.payment.adapter.ParticipantLink;
import com.ibm.lab.payment.dto.PaymentDto;
import com.ibm.lab.payment.model.ReservedPayment;
import com.ibm.lab.payment.service.PaymentService;

@RestController
public class PaymentController {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	
    private PaymentService paymentService;

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @PostMapping("/payments")
    public ResponseEntity<ParticipantLink> tryPayment(@RequestBody PaymentDto paymentDto) {
    	logger.info("tryPayment:{}", paymentDto);
        final ReservedPayment reservedPayment = paymentService.reservePayment(paymentDto);

        final ParticipantLink participantLink = buildParticipantLink(reservedPayment.getId(), reservedPayment.getExpires());

        return new ResponseEntity<>(participantLink, HttpStatus.CREATED);
    }

    private ParticipantLink buildParticipantLink(final Long id, final LocalDateTime expires) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

        return new ParticipantLink(location, expires);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDto>> getPayments() {
            List payments = paymentService.findAll();
            
            return new ResponseEntity<>(payments, HttpStatus.OK);
    }
        
    
    @PutMapping("/payments/{id}")
    public ResponseEntity<Void> confirmPayment(@PathVariable Long id) {
        try {
        	logger.info("put id:{}", id);
            paymentService.confirmPayment(id);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> cancelPayment(@PathVariable Long id) {
        paymentService.cancelPayment(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }    
}
