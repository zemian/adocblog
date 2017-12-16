package com.zemian.adocblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * A spring component to initialize webapp context
 */
@Component
public class WebInit {
    @Autowired
    private ServletContext ctx;

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        Map<String, Object> config = new HashMap<>();
        config.put("app.env", env.getProperty("app.env"));
        config.put("app.web.name", env.getProperty("app.web.name"));
        config.put("app.web.title", env.getProperty("app.web.title"));
        config.put("app.web.blogTitle", env.getProperty("app.web.blogTitle"));
        config.put("app.web.blogDateFormat", env.getProperty("app.web.blogDateFormat"));

        // Setup global context app variables for easy access in view layer.
        Map<String, Object> app = new HashMap<>();
        app.put("contextPath", ctx.getContextPath());
        app.put("config", config);

        // themeName is used often, so let's place it under 'app' for easy access
        app.put("themeName", env.getProperty("app.web.themeName"));

        ctx.setAttribute("app", app);
    }
}
