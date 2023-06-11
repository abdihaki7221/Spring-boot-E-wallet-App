package com.consume.consumeApi.controller;

import org.springframework.stereotype.Service;

import java.util.Random;

public class generateOtp {
    public String generateOtp() {
        Random random = new Random();
        int otp = random.nextInt(9000)+1000;
        return String.valueOf(otp);

    }
}
