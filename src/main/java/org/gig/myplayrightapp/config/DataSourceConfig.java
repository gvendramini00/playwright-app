package org.gig.myplayrightapp.config;

import com.zaxxer.hikari.HikariDataSource;
import org.gig.myplayrightapp.enums.Brand;
import org.gig.myplayrightapp.repository.BrandRoutingDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.util.*;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource cgmDataSource(
            @Value("${datasource.cgm.url}") String url,
            @Value("${datasource.cgm.username}") String user,
            @Value("${datasource.cgm.password}") String pass) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(user);
        ds.setPassword(pass);
        return ds;
    }

    @Bean
    public DataSource gpptDataSource(
            @Value("${datasource.gppt.url}") String url,
            @Value("${datasource.gppt.username}") String user,
            @Value("${datasource.gppt.password}") String pass) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(user);
        ds.setPassword(pass);
        return ds;
    }

    /** Primary DS used by JPA; routes per request via BrandContext. */
    @Primary
    @Bean
    public DataSource routingDataSource(DataSource cgmDataSource, DataSource gpptDataSource) {
        BrandRoutingDataSource routing = new BrandRoutingDataSource();
        Map<Object, Object> targets = new HashMap<>();
        targets.put(Brand.CGM, cgmDataSource);
        targets.put(Brand.GP_PT, gpptDataSource);
        routing.setTargetDataSources(targets);
        routing.setDefaultTargetDataSource(cgmDataSource); // default if none set
        return routing;
    }
}
