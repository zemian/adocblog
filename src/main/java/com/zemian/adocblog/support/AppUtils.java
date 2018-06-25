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
        Properties props = new Properties();
        Resource verResource = getEnvResource("version.properties");
        if (verResource == null) {
            // If we don't have the version file, just display static values
            props.setProperty("version", "SNAPSHOT");
            props.setProperty("commit-id", "HEAD");
            props.setProperty("build-date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        } else {
            Properties verProps = getResourceProperties(verResource);
            props.setProperty("version", verProps.getProperty("git.build.version"));
            props.setProperty("commit-id", verProps.getProperty("git.commit.id"));
            props.setProperty("build-date", verProps.getProperty("git.build.time"));

            // If it's SNAPSHOT version, shows the short commit id instead.
            if (props.getProperty("version").indexOf("SNAPSHOT") > 0) {
                String shortCommitId = props.getProperty("commit-id").substring(0, 7);
                props.setProperty("version", shortCommitId);
            }
        }
        return props;
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
