package com.consume.consumeApi.controller;

import com.consume.consumeApi.model.*;
import com.consume.consumeApi.repository.AccountsRepository;
import com.consume.consumeApi.services.accountsImpl;
import com.consume.consumeApi.services.userIntImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor


@CrossOrigin("http://127.0.0.1:5173")
@RequestMapping("/auth/api")
public class controller {
    private  final BCryptPasswordEncoder passwordEncoder;

    RestTemplate restTemplate = new RestTemplate();
    Cache<String, String> otpCache = Caffeine
            .newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();

    private final accountsImpl aService;
    private final AccountsRepository aRepo;

    private final userIntImpl uService;
    private final HttpServletRequest httpServletRequest;



    @GetMapping("/consumeApi")
    public ResponseEntity<List<String>> consumeApi() {


        String url = "http://localhost:8080/getUsers";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }
        );
        List<Map<String, Object>> userJsonList = response.getBody();
        List<String> emailList = new ArrayList<>();

        for (Map<String, Object> userJson : userJsonList) {
            String email = (String) userJson.get("email");
            emailList.add(email);
        }
        return ResponseEntity.ok().body(emailList);

    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        userModel userModel = uService.getUserByEmail(loginRequest.getEmail());
        if (userModel != null) {
            if (passwordEncoder.matches(loginRequest.getPassword(), userModel.getPassword())) {
                if (userModel.getIsVerified().equals(true)) {
                    return ResponseEntity.status(HttpStatus.OK).body(uService.logUsers(loginRequest));
                }else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("please verify ur email");

                }
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid credentials");
            }

        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user is not registered");
        }
    }

    @GetMapping("/profile")
    public  ResponseEntity<userModel> getEmail(){
        String auth_header = httpServletRequest.getHeader("Authorization");
        String token;
        if (auth_header.startsWith("Bearer")){
             token = auth_header.substring(7);
        }else {
             token =auth_header;
        }


        String extracted_email = uService.ExtractUsername(token);

        userModel userModel = uService.getUserByEmail(extracted_email);
        return ResponseEntity.ok(userModel);


    }
    @PostMapping("/send_money")
    public ResponseEntity<String> sendMoney(@RequestBody SendMoneyRequest sRequest){
        Long  account_no = sRequest.getAccount();
        int amount = sRequest.getAmount();
        String pin = sRequest.getPin();
        AccountsModel accountsModel =  aService.getByAccount_No(account_no);
        AccountsModel acModel = aService.getByAccount_No(sRequest.getOwner_account());
        if (accountsModel != null){
            if (passwordEncoder.matches(pin,acModel.getPin())){
                if (acModel.getBalance() >0 && amount <acModel.getBalance()){
                    acModel.setBalance(acModel.getBalance()-amount);
                    accountsModel.setBalance(accountsModel.getBalance()+amount);
                    aRepo.save(accountsModel);

                    return  ResponseEntity.status(HttpStatus.OK)
                            .body("Successfully Transferred Ksh " + amount +" to Account "
                                    + account_no
                            );

                }else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Your have insufficient Balance please deposit \n " +
                                    " Your current balance is Ksh " + accountsModel.getBalance());
                }
            }else {
                return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed! Invalid Pin");
            }
        }else {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with the Account "
                            + account_no + " does not exist");
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<AccountsModel> getBalance(){
        String auth_header = httpServletRequest.getHeader("Authorization");
        String token;
        if (auth_header.startsWith("Bearer")){
            token = auth_header.substring(7);
        }else {
            token =auth_header;
        }


        String extracted_email = uService.ExtractUsername(token);

        AccountsModel acc_model = aService.getUserByEmail(extracted_email);
        return ResponseEntity.ok(acc_model);


    }
}

