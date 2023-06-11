package com.consume.consumeApi.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResendOtpRequest {
    private String number;
}
