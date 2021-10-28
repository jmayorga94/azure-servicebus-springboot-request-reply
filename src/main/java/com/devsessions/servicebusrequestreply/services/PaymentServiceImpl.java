package com.devsessions.servicebusrequestreply.services;

import com.azure.core.util.BinaryData;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderAsyncClient;
import com.devsessions.servicebusrequestreply.entities.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements  PaymentService {

    final static String queueName = "sb.test.req";
    @Autowired
    BrokerMessageService brokerService;

    @Override
    public void SendPayment(String sessionId, Payment payment) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        String jsonPayment = mapper.writeValueAsString(payment);

        //Sending message to Queue
        brokerService.SendMessage(sessionId,jsonPayment,queueName);

    }

    @Override
    public String ReceivePaymentProcessed(String sessionId,String queueName) {

         String paymentProcessed = brokerService.ReceiveMessage(sessionId,queueName);

         if (paymentProcessed == null){
             return null;
         }

         return paymentProcessed;
    }
}
