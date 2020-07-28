package com.kr.media.amazing;

import com.kr.media.amazing.db.RoutingDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * @Autowired 是根据类型进行自动装配的。如果当Spring上下文中存在不止一个 UserDao 类型的 bean 时，就会抛出 eanCreationException 异常;
 * 如果 Spring 上下文中不存在 UserDao 类型的 bean，也会抛出 BeanCreationException 异常。我们可以使用 @Qualifier 配合 @Autowired 来解决这些问题
 */
@SpringBootApplication
public class MediaKrAmazingApplication {
    private final static Logger logger = LogManager.getLogger(MediaKrAmazingApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(MediaKrAmazingApplication.class, args);
    }


    @Bean
    @Primary
    DataSource primaryDataSource(@Autowired @Qualifier("masterDataSource") DataSource masterDataSource,
            @Autowired @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        logger.info("create routing datasource...");
        Map<Object, Object> map = new HashMap<>();
        map.put("masterDataSource", masterDataSource);
        map.put("slaveDataSource", slaveDataSource);
        RoutingDataSource routing = new RoutingDataSource();
        routing.setTargetDataSources(map);
        routing.setDefaultTargetDataSource(masterDataSource);
        return routing;
    }


    /**
     * Master data source.
     */
    @Bean("masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSource masterDataSource() {
        logger.info("create master datasource...");
        return DataSourceBuilder.create().build();
    }

    /**
     * Slave (read only) data source.
     */
    @Bean("slaveDataSource")
    @ConfigurationProperties(prefix = "spring.ro-datasource")
    DataSource slaveDataSource() {
        logger.info("create slave datasource...");
        return DataSourceBuilder.create().build();
    }


}
