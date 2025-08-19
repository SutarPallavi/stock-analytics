package com.stockanalytics.portfolio.repository;

import com.stockanalytics.portfolio.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    Optional<Account> findByUserId(String userId);
}
