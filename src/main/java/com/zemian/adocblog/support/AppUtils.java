package com.zemian.adocblog.support;

import com.zemian.adocblog.AppException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class AppUtils {
    public static final String APP_NAME = "adocblog";
    public static Properties getReleaseProps() {
        Properties relProps = new Properties();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(APP_NAME + "/release.properties");
        if (url == null) {
            relProps.setProperty("version", "SNAPSHOT");
            relProps.setProperty("commit-id", "HEAD");
            relProps.setProperty("build-date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        } else {
            try (InputStream inputStream = url.openStream()) {
                relProps.load(inputStream);
            } catch (IOException e) {
                throw new AppException("Failed to read release.properties", e);
            }
        }
        return relProps;
    }
}
