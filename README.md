# azure-servicebus-springboot-request-reply
Este repositorio tiene como objetivo mostrar un ejemplo del [Patrón Request-Reply](https://www.enterpriseintegrationpatterns.com/RequestReply.html) utilizando:
*  [Azure Service Bus](https://docs.microsoft.com/en-us/azure/service-bus-messaging/service-bus-java-how-to-use-queues)
*  [Sesiones Azure Service Bus](https://docs.microsoft.com/en-us/azure/service-bus-messaging/message-sessionsm)
*   Java 11

# Importante
Asegurate de configurar lo siguiente: 
1) Agrega tu conexión al Service Bus (Properties)
2) Crear cola de request con nombre sb.test.req (Sin sessiones) y crear cola de respuesta (habilitar sessiones) sb.test.rsp
