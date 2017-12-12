package com.zemian.adocblog.data;

import com.zemian.adocblog.CommonConfig;
import com.zemian.adocblog.cipher.Crypto;
import com.zemian.adocblog.cipher.CryptoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Import({CommonConfig.class, CryptoConfig.class})
@ComponentScan
public class DataConfig {

    @Autowired
    private Environment env;

    @Autowired
    private Crypto crypto;

    @Bean
    public DataSource dataSource() {
        String password = crypto.decrypt(env.getProperty("app.ds.password"));
        DriverManagerDataSource bean = new DriverManagerDataSource();
        bean.setDriverClassName(env.getProperty("app.ds.driverClassName"));
        bean.setUrl(env.getProperty("app.ds.url"));
        bean.setUsername(env.getProperty("app.ds.username"));
        bean.setPassword(password);

        return bean;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws Exception {
        JdbcTemplate bean = new JdbcTemplate(dataSource());
        return bean;
    }
}
