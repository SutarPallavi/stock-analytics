package com.stockanalytics.portfolio.repository;

import com.stockanalytics.portfolio.model.OrderExecuted;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderExecutedRepository extends MongoRepository<OrderExecuted, String> {

    List<OrderExecuted> findByAccountIdOrderByExecutionTimestampDesc(String accountId);
    
    List<OrderExecuted> findByAccountIdAndExecutionTimestampAfter(String accountId, Instant timestamp);
}
