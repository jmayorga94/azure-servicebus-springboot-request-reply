package com.devsessions.servicebusrequestreply.services;

import org.springframework.stereotype.Service;


public interface BrokerMessageService {
    public boolean SendMessage(String sessionId, String message, String queueName);
    public String ReceiveMessage(String sessionId,String queueName);
}
