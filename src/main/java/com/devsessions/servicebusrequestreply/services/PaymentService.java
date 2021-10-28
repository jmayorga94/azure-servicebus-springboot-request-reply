package com.devsessions.servicebusrequestreply.services;

import com.devsessions.servicebusrequestreply.entities.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentService {
    public void SendPayment(String SessionId, Payment payment) throws JsonProcessingException;
    public String ReceivePaymentProcessed(String sessionId, String queueName);
}
