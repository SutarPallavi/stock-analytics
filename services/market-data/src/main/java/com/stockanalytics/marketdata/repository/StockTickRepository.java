package com.stockanalytics.marketdata.repository;

import com.stockanalytics.shared.model.StockTick;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface StockTickRepository extends MongoRepository<StockTick, String> {

    List<StockTick> findBySymbolOrderByTimestampDesc(String symbol);

    @Query("{'symbol': ?0, 'timestamp': {$gte: ?1}}")
    List<StockTick> findBySymbolAndTimestampAfter(String symbol, Instant timestamp);

    @Query("{'timestamp': {$gte: ?0}}")
    List<StockTick> findByTimestampAfter(Instant timestamp);

    @Query(value = "{'symbol': ?0}", sort = "{'timestamp': -1}")
    StockTick findLatestBySymbol(String symbol);
}
