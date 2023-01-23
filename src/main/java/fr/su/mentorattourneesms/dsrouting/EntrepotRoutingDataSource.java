package fr.su.mentorattourneesms.dsrouting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class EntrepotRoutingDataSource extends AbstractRoutingDataSource {

    @Autowired
    ContextHolder contextHolder;


    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.getInstance().getLastUsedDataSourceIdentifier();
    }

}
