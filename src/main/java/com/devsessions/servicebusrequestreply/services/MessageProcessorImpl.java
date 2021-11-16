package com.devsessions.servicebusrequestreply.services;

import com.azure.messaging.servicebus.*;
import com.devsessions.servicebusrequestreply.config.ServiceBusConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class MessageProcessorImpl implements  MessageProcessor{


<<<<<<< HEAD
    private String connectionString = "Endpoint=sb://sb-ba-development.servicebus.windows.net/;SharedAccessKeyName=senderKey;SharedAccessKey=pLmDo6k+WmJDhjqGYt0JwzTQNIf3FjLfr1/WUCBtcjM=";
    @Autowired
    private BrokerMessageService brokerMessageService;
    final static String requestQueueName = "sb.test.req";
    final static String responseQueueName = "sb.test.rsp";
=======
    @Autowired
    private BrokerMessageService brokerMessageService;
    @Autowired
    private ServiceBusConfig serviceBusConfig;
    final static String requestQueueName = "sb.test.req"; //Cola de request
    final static String responseQueueName = "sb.test.rsp"; //Cola de response
>>>>>>> f53b877580c7646e7167bef49afc7b85f93cec5a

    public void ProcessMessage() throws InterruptedException {

        ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
<<<<<<< HEAD
                .connectionString(connectionString)
=======
                .connectionString(serviceBusConfig.getConnectionString())
>>>>>>> f53b877580c7646e7167bef49afc7b85f93cec5a
                .processor()
                .queueName(requestQueueName)
                .processMessage(context -> processMessage(context))
                .processError(context -> processError(context))
                .buildProcessorClient();

        processorClient.start();

        CountDownLatch countdownLatch = new CountDownLatch(1);
        if (countdownLatch.await(10, TimeUnit.SECONDS)) {
            System.out.println("Closing processor due to unretriable error");
        } else {
            System.out.println("Closing processor.");
        }
        
        processorClient.close();

    }

    private void processMessage(ServiceBusReceivedMessageContext context)
    {
        ServiceBusReceivedMessage message = context.getMessage();
        System.out.printf("Processing payment message. Session: %s, Sequence #: %s. Contents: %s%n", message.getMessageId(),
                message.getSequenceNumber(), message.getBody());

        String messageResponse = "payment OK";
        ServiceBusMessage serviceBusMessage = new ServiceBusMessage(messageResponse).setSessionId(message.getSessionId());
        brokerMessageService.SendMessage(message.getSessionId(),messageResponse,responseQueueName);

    }
    private void processError(ServiceBusErrorContext context) {
        System.out.printf("Error when receiving messages from namespace: '%s'. Entity: '%s'%n",
                context.getFullyQualifiedNamespace(), context.getEntityPath());

        if (!(context.getException() instanceof ServiceBusException)) {
            System.out.printf("Non-ServiceBusException occurred: %s%n", context.getException());
            return;
        }

        ServiceBusException exception = (ServiceBusException) context.getException();
        ServiceBusFailureReason reason = exception.getReason();

        if (reason == ServiceBusFailureReason.MESSAGING_ENTITY_DISABLED
                || reason == ServiceBusFailureReason.MESSAGING_ENTITY_NOT_FOUND
                || reason == ServiceBusFailureReason.UNAUTHORIZED) {
            System.out.printf("An unrecoverable error occurred. Stopping processing with reason %s: %s%n",
                    reason, exception.getMessage());

        } else if (reason == ServiceBusFailureReason.MESSAGE_LOCK_LOST) {
            System.out.printf("Message lock lost for message: %s%n", context.getException());
        } else if (reason == ServiceBusFailureReason.SERVICE_BUSY) {
            try {
                // Choosing an arbitrary amount of time to wait until trying again.
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Unable to sleep for period of time");
            }
        } else {
            System.out.printf("Error source %s, reason %s, message: %s%n", context.getErrorSource(),
                    reason, context.getException());
        }
    }
}
