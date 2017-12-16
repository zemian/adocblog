package com.zemian.adocblog.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemian.adocblog.service.DbPropsEnvironment;
import com.zemian.adocblog.web.listener.UserSessionInterceptor;
import com.zemian.adocblog.web.view.freemarker.PageTemplateLoader;
import no.api.freemarker.java8.Java8ObjectWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;

/**
 * Web MVC Settings
 */
@Configuration
@ComponentScan
@EnableWebMvc
public class AppWebConfig implements WebMvcConfigurer {

    @Value("${app.web.themeName}")
    private String themeName;

    @Autowired
    private freemarker.template.Configuration freemarkerConfig;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error").setViewName("error");
    }

    @Bean
    public PageTemplateLoader pageTemplateLoader() {
        PageTemplateLoader bean = new PageTemplateLoader();
        return bean;
    }

    @Bean
    public FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setPreTemplateLoaders(pageTemplateLoader());
        bean.setTemplateLoaderPath("/WEB-INF/ftl/");
        bean.setConfigLocation(new ClassPathResource("/adocblog/freemarker.properties"));
        return bean;
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setConfiguration(freeMarkerConfigurationFactoryBean().getObject());
        return configurer;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // The prefix is automatically inherit from FreeMarkerConfigurer bean.
        registry.freeMarker().suffix(".ftl");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");

        String themePath = "/themes/" + themeName;
        registry.addResourceHandler(themePath + "/**").addResourceLocations(themePath + "/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Protect all admin URLs
        registry.addInterceptor(userLoginInterceptor()).addPathPatterns("/admin/**");
    }

    @Bean
    public UserSessionInterceptor userLoginInterceptor() {
        UserSessionInterceptor bean =  new UserSessionInterceptor();
        bean.setLoginUrl("/login");
        return bean;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper bean = new ObjectMapper(); // JSON mapper
        return bean;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter bean = new MappingJackson2HttpMessageConverter(objectMapper());
        return bean;
    }

    @Bean
    public DbPropsEnvironment dbPropsEnvironment() {
        DbPropsEnvironment bean = new DbPropsEnvironment();
        return bean;
    }
}
