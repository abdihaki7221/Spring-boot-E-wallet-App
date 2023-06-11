package com.consume.consumeApi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendMoneyRequest {
    private  Long account;
    private Long owner_account;
    private  Integer amount;
    private String pin;
}
