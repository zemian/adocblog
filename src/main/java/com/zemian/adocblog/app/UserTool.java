package com.zemian.adocblog.app;

import com.zemian.adocblog.CommonConfig;
import com.zemian.adocblog.app.support.CmdOpts;
import com.zemian.adocblog.cipher.PasswordHasher;
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
public class UserTool {
    @Configuration
    @Import({ServiceConfig.class, CommonConfig.class})
    public static class Config {
        @Bean
        public UserTool userTool() {
            return new UserTool();
        }
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext spring = new AnnotationConfigApplicationContext(Config.class);
        UserTool main = spring.getBean(UserTool.class);
        main.run(args);
        spring.close();
    }

    private static Logger LOG = LoggerFactory.getLogger(UserTool.class);

    @Autowired
    private UserService userService;

    private void printHelp() {
        System.out.println("User management tool.\n" +
                "\n" +
                "Usage: (create new user)\n" +
                "  UserTool --create [--adminUser=true] [--fullName=NAME] <username> <password>\n" +
                "\n" +
                "Usage: (update new user)\n" +
                "  UserTool --update [--adminUser=true] [--fullName=NAME] <username> <password>\n" +
                "\n" +
                "Usage: (delete (mark) user)\n" +
                "  UserTool --delete <username>\n" +
                "\n" +
                "Usage: (print hashed password only)\n" +
                "  UserTool --hashPassword=PASSWORD\n" +
                "\n");
    }

    public void run(String[] args) {
        CmdOpts opts = new CmdOpts(args);

        if(opts.hasOpt("help")) {
            printHelp();
            System.exit(0);
        } else if(opts.hasOpt("hashPassword")) {
            String password = opts.getOpt("hashPassword");
            String encryptedPassword = PasswordHasher.createHash(password);
            System.out.println("Password: " + encryptedPassword);
            System.exit(0);
        } else if (opts.hasOpt("create") ||
                opts.hasOpt("update")) {
            if (opts.getArgsSize() < 2) {
                System.out.println("ERROR: Invalid arguments.");
                printHelp();
                System.exit(1);
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
        } else if (opts.hasOpt("delete")) {
            String username = opts.getArg(0);
            userService.markForDelete(username);
            LOG.info("{} has been marked for delete successfully.", username);
        } else {
            System.out.println("ERROR: Invalid options.");
            printHelp();
            System.exit(1);
        }
    }
}
