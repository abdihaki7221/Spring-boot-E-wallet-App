package com.consume.consumeApi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

public class CreateAccountRequest {


    private String number;
    private String email;
    private String pin;
    private String confirmPin;

}
