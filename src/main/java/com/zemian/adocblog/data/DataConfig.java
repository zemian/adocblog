package com.zemian.adocblog.data;

import com.zemian.adocblog.CommonConfig;
import com.zemian.adocblog.cipher.Crypto;
import com.zemian.adocblog.cipher.CryptoConfig;
import com.zemian.adocblog.support.AppUtils;
import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Import({CommonConfig.class, CryptoConfig.class})
@ComponentScan
public class DataConfig {

    @Autowired
    private Environment env;

    @Autowired
    private Crypto crypto;

    @Value("${app.ds.disablePasswordDecryption}")
    private boolean disablePasswordDecryption;

    @Bean
    public DataSource dataSource() throws Exception {
        // Setup poolProps
        Properties poolProps = AppUtils.getResourceProperties("ds-pool-tomcat.properties");

        // Override few main props of driver, url, username and password values from
        // the app.properties directly into the poolProps
        String password = env.getProperty("app.ds.password");
        if (!disablePasswordDecryption) {
            password = crypto.decrypt(password);
        }

        poolProps.put("driverClassName", env.getProperty("app.ds.driverClassName"));
        poolProps.put("url", env.getProperty("app.ds.url"));
        poolProps.put("username", env.getProperty("app.ds.username"));
        poolProps.put("password", password);

        PoolConfiguration poolConfig = DataSourceFactory.parsePoolProperties(poolProps);

        // Setup additional JDBC Driver Connection properties if there are any
        Properties dbProps = AppUtils.getResourceProperties("ds-driver-postgres.properties");
        poolConfig.setDbProperties(dbProps);

        // Now create the actual DataSource from poolConfig
        org.apache.tomcat.jdbc.pool.DataSource bean =
                new org.apache.tomcat.jdbc.pool.DataSource(poolConfig);

        // Initialize the pool now
        bean.createPool();

        return bean;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws Exception {
        JdbcTemplate bean = new JdbcTemplate(dataSource());
        return bean;
    }
}
