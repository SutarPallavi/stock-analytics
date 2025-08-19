package com.stockanalytics.alerts.repository;

import com.stockanalytics.shared.model.AlertNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AlertNotificationRepository extends MongoRepository<AlertNotification, String> {

    List<AlertNotification> findByRuleIdOrderByFiredTsDesc(String ruleId);
    
    List<AlertNotification> findByAccountIdOrderByFiredTsDesc(String accountId);
    
    List<AlertNotification> findByFiredTsAfter(Instant timestamp);
}
