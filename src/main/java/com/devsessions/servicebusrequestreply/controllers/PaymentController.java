package com.devsessions.servicebusrequestreply.controllers;

import com.devsessions.servicebusrequestreply.entities.Payment;
import com.devsessions.servicebusrequestreply.services.MessageProcessor;
import com.devsessions.servicebusrequestreply.services.MessageProcessorImpl;
import com.devsessions.servicebusrequestreply.services.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class PaymentController {

    @Autowired
    PaymentService paymentService;
    @Autowired
    MessageProcessor messageProcessor;
    final static String responseQueueName = "sb.test.rsp";
    @RequestMapping(value = "/payment/pay",method =  RequestMethod.POST)
    public ResponseEntity<Object> createPayment(@RequestBody Payment payment) throws JsonProcessingException {

        try {

            String sessionId = UUID.randomUUID().toString();

            //Sending message
            paymentService.SendPayment(sessionId, payment);

            //Processing message

            messageProcessor.ProcessMessage();

            //Receiving message
            String response = paymentService.ReceivePaymentProcessed(sessionId, responseQueueName);

            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
