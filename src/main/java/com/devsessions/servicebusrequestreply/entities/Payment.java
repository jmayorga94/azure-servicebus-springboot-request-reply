package com.devsessions.servicebusrequestreply.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Payment {

   public Payment ()
   {
     this.correlationId = UUID.randomUUID();
   }
    private UUID correlationId;
    private String currency;
    private BigDecimal amount;
    private String  userName;
}
