package org.gig.myplayrightapp.repository;

import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.model.BrandContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class BrandRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("DetermineCurrentLookupKey " + BrandContext.get());
        return BrandContext.get();
    }
}
