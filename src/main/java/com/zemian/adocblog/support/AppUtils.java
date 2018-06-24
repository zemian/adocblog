package com.zemian.adocblog.support;

import com.zemian.adocblog.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class AppUtils {
    public static final String APP_NAME = "adocblog";
    public static final String APP_ENV_KEY = APP_NAME + ".env";

    /*
    Get application env name OR null if not found.
     */
    public static String getEnvName() {
        return System.getProperty(APP_ENV_KEY);
    }

    /*
    Return the first found resource under the application env or root of namespace, OR null if none is found.
     */
    public static Resource getEnvResource(String resourceName) {
        ClassPathResource envResource = null;
        String envName = getEnvName();
        if (envName != null) {
            String resName = "/" + APP_NAME + "/" + envName + "/" + resourceName;
            envResource = new ClassPathResource(resName);
        }

        // If envResource is null, then default to app namespace path
        if (envResource == null || !envResource.exists()) {
            String resName = "/" + APP_NAME + "/" + resourceName;
            envResource = new ClassPathResource(resName);
        }

        if (!envResource.exists()) {
            envResource = null; // reset back to null if not exists
        }

        Logger LOG = LoggerFactory.getLogger(AppUtils.class);
        LOG.debug("Found env resourceName={} with result={}", resourceName, envResource);
        return envResource;
    }

    public static Properties getVersionProps() {
        Properties relProps = new Properties();
        Resource relResource = getEnvResource("version.properties");
        if (relResource == null) {
            relProps.setProperty("version", "SNAPSHOT");
            relProps.setProperty("commit-id", "HEAD");
            relProps.setProperty("build-date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        } else {
            Properties verProps = getResourceProperties(relResource);
            relProps.setProperty("version", verProps.getProperty("git.build.version"));
            relProps.setProperty("commit-id", verProps.getProperty("git.commit.id"));
            relProps.setProperty("build-date", verProps.getProperty("git.build.time"));
        }
        return relProps;
    }

    public static Properties getResourceProperties(String propsResourceName) {
        Resource resource = getEnvResource(propsResourceName);
        if (resource == null) {
            throw new AppException("Properties env resource " + propsResourceName + " not found.");
        }
        return getResourceProperties(resource);
    }

    public static Properties getResourceProperties(Resource resource) {
        Properties ret = new Properties();
        try (InputStream inputStream = resource.getInputStream()) {
            ret.load(inputStream);
        } catch (IOException e) {
            throw new AppException("Failed to load properties env resource " + resource.getFilename(), e);
        }

        return ret;
    }
}
