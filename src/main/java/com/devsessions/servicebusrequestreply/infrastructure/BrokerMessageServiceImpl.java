package com.devsessions.servicebusrequestreply.infrastructure;

import com.azure.core.util.IterableStream;
import com.azure.messaging.servicebus.*;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.devsessions.servicebusrequestreply.config.ServiceBusConfig;
import com.devsessions.servicebusrequestreply.exceptions.ServiceBusErrorException;
import com.devsessions.servicebusrequestreply.services.BrokerMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

<<<<<<< HEAD
//@Service
=======
@Service
>>>>>>> f53b877580c7646e7167bef49afc7b85f93cec5a
public class BrokerMessageServiceImpl implements BrokerMessageService {

    @Autowired
    private ServiceBusConfig serviceBusConfig;

    private String responseMessage;
    @Override
    public boolean SendMessage(String sessionId, String message, String queueName) {

        ServiceBusSenderAsyncClient sender = new ServiceBusClientBuilder()
                .connectionString(serviceBusConfig.getConnectionString())
                .sender()
                .queueName(queueName)
                .buildAsyncClient();
        try {
            //SetSessionId
            ServiceBusMessage serviceBusMessage = new ServiceBusMessage(message).setSessionId(sessionId);
            serviceBusMessage.getApplicationProperties().put("customProperty", "payment-online");

            Disposable subscribe = sender.sendMessage(serviceBusMessage).subscribe(
                    unused -> System.out.println("Message Sent."),
                    error -> System.err.println("Error occurred while publishing message: " + error),
                    () -> {
                        System.out.println("Send complete.");
                    });

            return true;
        }
        catch (ServiceBusErrorException exception){
            throw new ServiceBusErrorException("Error enviando mensaje a service bus:"+message,exception);
        }
        catch (Exception ex){
            throw ex;
        }
        finally {
            //sender.close();
        }
    }

    @Override
    public String ReceiveMessage(String sessionId,String queueName) {
        ServiceBusSessionReceiverClient sessionReceiver = new ServiceBusClientBuilder()
                .connectionString(serviceBusConfig.getConnectionString())
                .sessionReceiver()
                .queueName(queueName)
                .buildClient();


        ServiceBusReceiverClient receiver = sessionReceiver.acceptSession(sessionId);

        try {
                IterableStream<ServiceBusReceivedMessage> messages = receiver.receiveMessages(1);

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
        } finally {
            // Dispose of our resources.
            receiver.close();
        }

        return responseMessage;
    }

}
