package com.consume.consumeApi.services;

import com.consume.consumeApi.model.LoginRequest;
import com.consume.consumeApi.model.RegisterRequest;
import com.consume.consumeApi.model.RegisterResponse;
import com.consume.consumeApi.model.userModel;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface userInt {


    RegisterResponse addUsers(RegisterRequest request);
    RegisterResponse logUsers(LoginRequest loginRequest);

    userModel getUserByEmail(String email);

    List<userModel> FindAllUserEmails(String email);

    String generatedToken(UserDetails userDetails);

    String ExtractUsername(String token);

    userModel getUserByNumber(String phoneNumber);


}
