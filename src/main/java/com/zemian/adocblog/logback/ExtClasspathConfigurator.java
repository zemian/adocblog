package com.zemian.adocblog.logback;

import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.zemian.adocblog.AppException;
import com.zemian.adocblog.support.AppUtils;

import java.net.URL;

/**
 * Logback will automatically look for a classpath:/META-INF/services/ch.qos.logback.classic.spi.Configurator
 * file and if you add this class name into this file, it will look for "logbcack.xml" file under our
 * own application namespace in the classpath. If no "logbcack.xml"  file is found, it will
 * default to "BasicConfigurator".
 *
 * This custom configuration loader is created so we can consolidate all properties and related files under
 * our application namespace in classpath for good practice purpose.
 */
public class ExtClasspathConfigurator extends JoranConfigurator implements Configurator {

    @Override
    public void configure(LoggerContext loggerContext) {
        // Get logback.xml from the application's namespace in classpath. If an env name is given, it will be
        // added as suffix with "_" as separator, else it will be empty string.
        //
        // Example: (default)
        //  classpath:/<appname>/logback.xml
        //
        // Example: -D<appname>.env=qa
        //   classpath:/<appname>/logback-qa.xml
        String env = System.getProperty(AppUtils.APP_NAME + ".env");
        String resource = AppUtils.APP_NAME + ((env != null) ? "_" + env : "") +"/logback.xml";
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(resource);

        try {
            if (url != null) {
                doConfigure(url);
            } else {
                new BasicConfigurator().configure(loggerContext);
            }
        } catch (JoranException e) {
            throw new AppException("Failed to load logback config " + url, e);
        }
    }
}
