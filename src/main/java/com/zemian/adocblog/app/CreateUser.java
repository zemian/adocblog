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
            throw new AppException("Wrong args: <username> <password> [admin]");
        }

        String username = args[0];
        String password = args[1];
        boolean isAdmin = false;

        if (args.length == 3) {
            isAdmin = true;
        }

        LOG.info("Create new user: {}", username);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAdmin(isAdmin);
        userService.create(user);

        LOG.info("{} has been created successfully.", user);
    }
}
