package com.consume.consumeApi.repository;

import com.consume.consumeApi.model.AccountsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<AccountsModel, Long> {

    AccountsModel findByAccount(Long Account);

    AccountsModel findByEmail(String extractedEmail);
}
