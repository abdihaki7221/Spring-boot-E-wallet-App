package com.consume.consumeApi.repository;

import com.consume.consumeApi.model.userModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepo extends JpaRepository<userModel, Integer> {

    userModel findByEmail(String email);

    userModel findByNumber(String number);
}
