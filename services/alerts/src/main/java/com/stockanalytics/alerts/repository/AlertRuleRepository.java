package com.stockanalytics.alerts.repository;

import com.stockanalytics.alerts.model.AlertRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRuleRepository extends MongoRepository<AlertRule, String> {

    List<AlertRule> findByAccountId(String accountId);
    
    List<AlertRule> findBySymbolAndEnabledTrue(String symbol);
    
    List<AlertRule> findByAccountIdAndSymbol(String accountId, String symbol);
}
