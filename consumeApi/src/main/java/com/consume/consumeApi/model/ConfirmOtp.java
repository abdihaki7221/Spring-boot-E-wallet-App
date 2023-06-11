package com.consume.consumeApi.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ConfirmOtp {
    private String number;
    private  String Otp;
}
