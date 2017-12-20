package com.zemian.adocblog.service;

import com.zemian.adocblog.data.domain.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This service will load all records from settings table with category match to
 * APP_CONFIG_CATEGORY_PREFIX + [app.env], where [app.env] must be defined in 'app.properties' file first.
 * These DB settings will override the one found in 'app.properties'.
 */
public class DbPropsEnvironment {
    private static Logger LOG = LoggerFactory.getLogger(DbPropsEnvironment.class);

    public static final String APP_WEB_CATEGORY_PREFIX = "APP_WEB_";
    public static final String APP_ENV_KEY = "app.env";

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    @Autowired
    private SettingService settingService;

    @PostConstruct
    public void init() {
        String category = APP_WEB_CATEGORY_PREFIX + configurableEnvironment.getProperty(APP_ENV_KEY);
        LOG.info("Loading {} from settings db table.", category);
        Map<String, Object> dbSettings = settingService.findByCategory(category).stream().collect(
                Collectors.toMap(Setting::getName, s -> s.getValue()));

        LOG.debug("Adding {} settings into Spring env", dbSettings.size());
        MapPropertySource dbSettingsSource = new MapPropertySource("dbSettings", dbSettings);
        configurableEnvironment.getPropertySources().addFirst(dbSettingsSource);
    }
}
