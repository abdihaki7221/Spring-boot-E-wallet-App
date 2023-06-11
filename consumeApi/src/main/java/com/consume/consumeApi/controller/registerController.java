package com.consume.consumeApi.controller;

import com.consume.consumeApi.model.*;
import com.consume.consumeApi.repository.userRepo;
import com.consume.consumeApi.services.accountsImpl;
import com.consume.consumeApi.services.userIntImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/auth/api")
public class registerController {

    RestTemplate restTemplate = new RestTemplate();

    Cache<String, String> otpCache = Caffeine
            .newBuilder()
            .expireAfterWrite(90, TimeUnit.SECONDS)
            .build();

    private final userIntImpl uService;
    private final userRepo uRepo;
     private  final accountsImpl aService;

    sendOtp sOtp = new sendOtp();
    generateOtp gOtp = new generateOtp();

    @PostMapping("/register")
    public ResponseEntity<?> postUsers(@RequestBody RegisterRequest user, Model model) {

        userModel userModel = uService.getUserByEmail(user.getEmail());

        if (userModel != null){
            return ResponseEntity.badRequest().body("user already exists");
        }
        String phone = user.getNumber();

        String otp = gOtp.generateOtp();
        otpCache.put(phone,otp);
        sOtp.sendOtpToPhone(phone,otp);
        return ResponseEntity.ok(uService.addUsers(user));

    }
    //resend otp
    @PostMapping("/Re-Send-otp")
    public ResponseEntity<String> resendOtp(@RequestBody RegisterRequest user) {

        String phone = user.getNumber();

        // Check if the email already exists in the database
        userModel userModel = uService.getUserByNumber(phone);

        System.out.println(userModel);
        if (userModel != null) {

            String otp = gOtp.generateOtp();
            otpCache.put(phone,otp);

            sOtp.sendOtpToPhone(phone,otp);
            System.out.println(otp);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid otp");
        }


    }

    //verify otp

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody ConfirmOtp confirmOtp){
        String phone_number = confirmOtp.getNumber();
        String entered_otp = confirmOtp.getOtp();

        String cached_otp = otpCache.getIfPresent(phone_number);
        if (cached_otp != null && cached_otp.equals(entered_otp)){
            userModel userModel = uService.getUserByNumber(phone_number);
            if (userModel != null){
                userModel.setIsVerified(true);
                uRepo.save(userModel);
            }
            return  ResponseEntity.ok("Otp verification successful");
        }else {
            return ResponseEntity.badRequest().body("Invalid Otp");
        }


    }


    @PostMapping("/create_account")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest cRequest) {
        String email = cRequest.getEmail();
        String pin = String.valueOf(cRequest.getPin());
        String confirm_pin = String.valueOf(cRequest.getConfirmPin());

        userModel userModel = uService.getUserByEmail(email);

        System.out.println(userModel);
        if (pin.equals(confirm_pin)) {
            if (userModel != null) {
                return ResponseEntity.status(HttpStatus.OK).body(aService.addAccount(cRequest));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user with email " +
                    cRequest.getEmail() + "is not registered");
        }
        else {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("the pin do not match");
        }
    }





}
