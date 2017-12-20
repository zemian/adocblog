package com.zemian.adocblog.web;

import com.zemian.adocblog.support.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
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
@DependsOn("dbPropsEnvironment")
public class WebInit {
    @Autowired
    private ServletContext ctx;

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        Map<String, Object> config = new HashMap<>();
        config.put("app.name", env.getProperty("app.name"));
        config.put("app.env", env.getProperty("app.env"));
        config.put("app.web.name", env.getProperty("app.web.name"));
        config.put("app.web.themeName", env.getProperty("app.web.themeName"));
        config.put("app.web.title", env.getProperty("app.web.title"));
        config.put("app.web.blogTitle", env.getProperty("app.web.blogTitle"));
        config.put("app.web.blogDateFormat", env.getProperty("app.web.blogDateFormat"));
        config.put("app.web.disqus.websiteName", env.getProperty("app.web.disqus.websiteName"));

        // Setup global context app variables for easy access in view layer.
        Map<String, Object> app = new HashMap<>();
        app.put("contextPath", ctx.getContextPath());
        app.put("config", config);

        // themeName is used often, so let's place it under 'app' for easy access
        app.put("themeName", env.getProperty("app.web.themeName"));

        // Add release info
        app.put("release", AppUtils.getReleaseProps());

        ctx.setAttribute("app", app);
    }
}
