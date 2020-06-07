package com.ibm.lab.payment.dto;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PaymentDto {
	private String orderId;
	private Long amt;

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Long getAmt() {
		return amt;
	}
	public void setAmt(Long amt) {
		this.amt = amt;
	}

	public static PaymentDto deserializeJSON(final String json) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, PaymentDto.class);
	}

	public String serializeJSON() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(this);
	}

	public void validate() {
		if(this.amt >= 300000L) {
			throw new IllegalArgumentException("결제 제한 금액 초과");
		}
	}
	
    @Override
    public String toString() {
        return "Stock{" +
                "orderId=" + orderId +
                ", amt=" + amt +
                '}';
    }
}
