package com.zemian.adocblog.service;

import com.zemian.adocblog.data.domain.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This service will load all records from settings table with category match to
 * APP_CONFIG_CATEGORY_PREFIX + [app.env], where [app.env] must be defined in 'application.properties' file first.
 * These DB settings will override the one found in 'application.properties'.
 */
public class DbPropsEnvironment implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger LOG = LoggerFactory.getLogger(DbPropsEnvironment.class);

    public static final String APP_CONFIG_CATEGORY_PREFIX = "APP_CONFIG_";
    public static final String APP_ENV_KEY = "app.env";

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    @Autowired
    private SettingService settingService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String category = APP_CONFIG_CATEGORY_PREFIX + configurableEnvironment.getProperty(APP_ENV_KEY);
        LOG.info("Loading {} from settings db table.", category);
        Map<String, Object> configProps = settingService.findByCategory(category).stream().collect(
                Collectors.toMap(Setting::getName, s -> s.getValue()));
        configurableEnvironment.getPropertySources().addFirst(new MapPropertySource("db", configProps));
    }
}
