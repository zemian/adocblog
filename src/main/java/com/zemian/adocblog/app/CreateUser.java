package com.zemian.adocblog.app;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.CommonConfig;
import com.zemian.adocblog.data.domain.User;
import com.zemian.adocblog.service.ServiceConfig;
import com.zemian.adocblog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Create a user (author) for the application to manage content.
 *
 * You may use "--adminUser=true" option to create admin users.
 */
public class CreateUser {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext spring = new AnnotationConfigApplicationContext(Config.class);
        CreateUser main = spring.getBean(CreateUser.class);
        main.run(args);
        spring.close();
    }

    @Configuration
    @Import({ServiceConfig.class, CommonConfig.class})
    public static class Config {
        @Bean
        public CreateUser createUser() {
            return new CreateUser();
        }
    }

    private static Logger LOG = LoggerFactory.getLogger(CreateUser.class);

    @Autowired
    private UserService userService;

    public void run(String[] args) {
        if (args.length < 2) {
            throw new AppException("Wrong args:" +
                    " java [-DadminUser=true] [-DfirstName=NAME] [-DlastName=NAME] <username> <password>");
        }

        String username = args[0];
        String password = args[1];

        boolean isAdmin = Boolean.parseBoolean(System.getProperty("adminUser", "false"));

        // Use two sys props to set full name since script is having hard time set sys props with space!
        String firstName = System.getProperty("firstName", "User ");
        String lastName = System.getProperty("lastName", username);
        String fullName = firstName + " " + lastName;

        LOG.info("Create new user: {}", username);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAdmin(isAdmin);
        user.setFullName(fullName);
        userService.create(user);

        LOG.info("{} has been created successfully.", user);
    }
}
