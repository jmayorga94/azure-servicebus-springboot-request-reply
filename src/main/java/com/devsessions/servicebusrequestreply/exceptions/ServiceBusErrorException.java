package com.devsessions.servicebusrequestreply.exceptions;

public class ServiceBusErrorException extends  RuntimeException{
    public ServiceBusErrorException(String errorMessage,Throwable err ){
        super(errorMessage,err);
    }
}
