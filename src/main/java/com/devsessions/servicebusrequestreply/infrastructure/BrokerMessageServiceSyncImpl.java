package com.devsessions.servicebusrequestreply.infrastructure;

import com.azure.core.util.IterableStream;
import com.azure.messaging.servicebus.*;
import com.devsessions.servicebusrequestreply.config.ServiceBusConfig;
import com.devsessions.servicebusrequestreply.services.BrokerMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class BrokerMessageServiceSyncImpl  implements BrokerMessageService  {

    @Autowired
    private ServiceBusConfig serviceBusConfig;
    private String responseMessage;
    @Override
    public boolean SendMessage(String sessionId, String message, String queueName) {

        ServiceBusSenderClient sender = null;
        try {

            sender = new ServiceBusClientBuilder()
                    .connectionString(serviceBusConfig.getConnectionString())
                    .sender()
                    .queueName(queueName)
                    .buildClient();
            ServiceBusMessage serviceBusMessage = new ServiceBusMessage(message);
            serviceBusMessage.setSessionId(sessionId);

            sender.sendMessage(serviceBusMessage);

        } catch (Exception e) {
            throw e;

        }
    finally {
            sender.close();
        }

        return  true;
    }

    @Override
    public String ReceiveMessage(String sessionId, String queueName) {
        ServiceBusSessionReceiverClient sessionReceiver = null;
        ServiceBusReceiverClient receiver = null;
        try {

            sessionReceiver = new ServiceBusClientBuilder()
                    .connectionString(serviceBusConfig.getConnectionString())
                    .sessionReceiver()
                    .queueName(queueName)
                    .buildClient();

            sessionReceiver.acceptSession(sessionId);

             receiver = sessionReceiver.acceptSession(sessionId);

            IterableStream<ServiceBusReceivedMessage> messages = receiver.receiveMessages(1, Duration.ofSeconds(5));

            for (ServiceBusReceivedMessage message : messages) {
                // Process message.

                responseMessage = message.getBody().toString();
                boolean isSuccessfullyProcessed = true;
                // Messages from the sync receiver MUST be settled explicitly. In this case, we complete the message if
                // it was successfully
                if (isSuccessfullyProcessed) {
                    receiver.complete(message);
                } else {
                    receiver.abandon(message, null);
                }
            }

            return responseMessage;
        } catch (Exception e) {
            throw e;

        }
        finally {
            sessionReceiver.close();

        }
    }
}
