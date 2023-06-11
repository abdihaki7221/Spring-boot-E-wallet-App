package com.consume.consumeApi.services;

import com.consume.consumeApi.model.AccountsModel;
import com.consume.consumeApi.model.CreateAccountRequest;

public interface accountsInt {

    String  addAccount(CreateAccountRequest cRequest);

    AccountsModel  getByAccount_No(Long account);

    AccountsModel getUserByEmail(String extractedEmail);
}
