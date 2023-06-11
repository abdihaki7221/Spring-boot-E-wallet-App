package com.consume.consumeApi.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Data
@Builder
public class LoginRequest {

    private  String email;

    private String password;


}
