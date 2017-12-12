package com.zemian.adocblog.service;

import com.zemian.adocblog.data.DataConfig;
import org.asciidoctor.Asciidoctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import javax.sql.DataSource;

@Configuration
@Import(DataConfig.class)
@ComponentScan
@EnableTransactionManagement
public class ServiceConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager bean = new DataSourceTransactionManager();
        bean.setDataSource(dataSource);
        return bean;
    }

    @Bean
    public Asciidoctor asciidoctor() {
        Asciidoctor bean = Asciidoctor.Factory.create();
        return bean;
    }

//    @Bean
//    public PageTemplateLoader pageTemplateLoader() {
//        PageTemplateLoader bean = new PageTemplateLoader();
//        return bean;
//    }
//
//    @Bean("serviceFreeMarkerConfigurationFactoryBean")
//    public FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean() {
//        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
//        bean.setPostTemplateLoaders(pageTemplateLoader());
//        return bean;
//    }
}
