package com.consume.consumeApi.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data

@Builder
public class RegisterResponse {
    private String token;
}
