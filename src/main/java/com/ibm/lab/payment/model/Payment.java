package com.ibm.lab.payment.model;

public class Payment {
	private Long id;

    private String orderId;

    private Long amt;

    public Payment() {
    }

    public Payment(String orderId, Long paymentAmt) {
        this.orderId = orderId;
        this.amt = paymentAmt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void getOrderId(String orderId) {
        this.orderId = orderId;
    }


    public Long getAmt() {
        return amt;
    }
    
    public void setAmt(Long amt) {
        this.amt = amt;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", paymentAmt=" + amt +
                '}';
    }
}
