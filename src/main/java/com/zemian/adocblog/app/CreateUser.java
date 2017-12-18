package com.zemian.adocblog.app;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.CommonConfig;
import com.zemian.adocblog.app.support.CmdOpts;
import com.zemian.adocblog.cipher.Crypto;
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

    @Autowired
    private Crypto crypto;

    private void printHelp() {
        System.out.println("User management tool.\n" +
                "\n" +
                "Usage: (create new user)\n" +
                "  java [--adminUser=true] [--fullName=NAME] <username> <password>\n" +
                "\n" +
                "Usage: (update new user)\n" +
                "  java --update [--adminUser=true] [--fullName=NAME] <username> <password>\n" +
                "\n" +
                "Usage: (print encrypted password only)\n" +
                        "  java --encryptPassword=PASSWORD\n"
        );
    }

    public void run(String[] args) {
        CmdOpts opts = new CmdOpts(args);

        if(opts.hasOpt("encryptPassword")) {
            String password = opts.getOpt("encryptPassword");
            String encryptedPassword = crypto.encrypt(password);
            System.out.println("Password: " + encryptedPassword);
            System.exit(0);
        } else {
            if (opts.getArgsSize() < 2) {
                throw new AppException("Wrong args:" +
                        " ");
            }

            String username = opts.getArg(0);
            String password = opts.getArg(1);

            boolean isAdmin = opts.getBooleanOpt("adminUser", false);
            String fullName = opts.getOpt("fullName", "User " + username);

            if (opts.hasOpt("update")) {
                LOG.debug("Updating existing user: {}", username);

                User user = userService.get(username);
                user.setPassword(password);
                user.setAdmin(isAdmin);
                user.setFullName(fullName);

                userService.update(user);

                LOG.info("{} (fullName={}) has been updated successfully.", user, fullName);
            } else {
                LOG.debug("Creating new user: {}", username);

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setAdmin(isAdmin);
                user.setFullName(fullName);
                userService.create(user);

                LOG.info("{} (fullName={}) has been created successfully.", user, fullName);
            }
        }
    }
}
