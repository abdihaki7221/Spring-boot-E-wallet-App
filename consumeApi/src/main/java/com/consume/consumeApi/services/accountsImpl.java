package com.consume.consumeApi.services;

import com.consume.consumeApi.model.AccountsModel;
import com.consume.consumeApi.model.CreateAccountRequest;
import com.consume.consumeApi.repository.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class accountsImpl implements accountsInt {
    private final AccountsRepository aRepo;
    private  final PasswordEncoder passwordEncoder;

    @Override
    public String  addAccount(CreateAccountRequest cRequest) {

        var user = AccountsModel.builder()
                .number(cRequest.getNumber())
                .email(cRequest.getEmail())
                .pin(passwordEncoder.encode(cRequest.getPin()))
                .build();
        Random random = new Random();
        Long min = 9000000000L;
        Long max = 1000000000L;

        Long acc_no = random.nextLong(min) +max;


        user.setAccount(acc_no);
        user.setBalance(10000);
        aRepo.save(user);
       return "success";


    }

    @Override
    public AccountsModel getByAccount_No(Long account) {
        return  aRepo.findByAccount(account);
    }

    @Override
    public AccountsModel getUserByEmail(String extractedEmail) {
        return  aRepo.findByEmail(extractedEmail);
    }

}
