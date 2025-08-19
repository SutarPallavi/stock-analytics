package com.stockanalytics.portfolio.repository;

import com.stockanalytics.portfolio.model.Position;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends MongoRepository<Position, String> {

    List<Position> findByAccountId(String accountId);
    
    Optional<Position> findByAccountIdAndSymbol(String accountId, String symbol);
}
