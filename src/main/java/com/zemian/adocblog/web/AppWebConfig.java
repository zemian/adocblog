package com.zemian.adocblog.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemian.adocblog.service.DbPropsEnvironment;
import com.zemian.adocblog.web.listener.UserSessionInterceptor;
import com.zemian.adocblog.web.view.freemarker.PageTemplateLoader;
import com.zemian.adocblog.web.view.freemarker.PageTemplateMethodModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.Map;

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
    private Environment env;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error").setViewName("error");

        // Map admin related urls
        registry.addViewController("/admin/restricted").setViewName("/admin/restricted");
    }

    @Bean
    public PageTemplateLoader pageTemplateLoader() {
        PageTemplateLoader bean = new PageTemplateLoader();
        return bean;
    }

    @Bean
    public PageTemplateMethodModel pageTemplateMethodModel() {
        PageTemplateMethodModel bean = new PageTemplateMethodModel();
        return bean;
    }

    @Bean
    public FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean() {
        Map<String, Object> sharedVars = new HashMap<>();
        sharedVars.put("getPage", pageTemplateMethodModel());

        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setPreTemplateLoaders(pageTemplateLoader());
        bean.setTemplateLoaderPath("/WEB-INF/ftl/");
        bean.setConfigLocation(new ClassPathResource("/adocblog/freemarker.properties"));
        bean.setFreemarkerVariables(sharedVars);
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

        registry.addResourceHandler("/favicon.ico").addResourceLocations("/favicon.ico");

        // Setup optional extra web static content that might be from outside of this war structure
        String staticDir = env.getProperty("app.web.static.directory", (String) null);
        String staticPath = env.getProperty("app.web.static.path", (String) null);
        if (staticDir != null && staticPath != null) {
            registry.addResourceHandler(staticPath + "/**")
                    .addResourceLocations("file:" + staticDir + "/");
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Protect all admin URLs - for any logged in users
        registry.addInterceptor(userLoginInterceptor()).addPathPatterns("/admin/**");

        // Protect admin users only URLs - for 'admin=true' users
        registry.addInterceptor(adminUserLoginInterceptor()).
                addPathPatterns("/admin/page/**",
                        "/admin/settings/**",
                        "/admin/system-info**");
    }

    @Bean("userLoginInterceptor")
    public UserSessionInterceptor userLoginInterceptor() {
        UserSessionInterceptor bean =  new UserSessionInterceptor();
        bean.setLoginUrl("/login");
        bean.setCheckIsUserAdmin(false);
        return bean;
    }

    @Bean("adminUserLoginInterceptor")
    public UserSessionInterceptor adminUserLoginInterceptor() {
        UserSessionInterceptor bean =  new UserSessionInterceptor();
        bean.setLoginUrl("/login");
        bean.setCheckIsUserAdmin(true);
        bean.setAdminRestrictedUrl("/admin/restricted");
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
