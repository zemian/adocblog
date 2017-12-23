package com.zemian.adocblog.service;

import com.zemian.adocblog.SpringTestBase;
import com.zemian.adocblog.data.dao.UserDAO;
import com.zemian.adocblog.data.domain.User;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@ContextConfiguration(classes = ServiceConfig.class)
public class UserServiceTest extends SpringTestBase {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDAO userDAO;

    private User createUser(String username, String password, String fullName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        return user;
    }

    @Test
    public void crud() throws Exception {
        String username = "UserServiceTest";
        try {
            assertThat(userService.exists(username), is(false));
            User test = createUser(username, "UserServiceTest", "Test UserServiceTest");
            userService.create(test);
            assertThat(userService.exists(username), is(true));

            User test2 = userService.get("UserServiceTest");
            assertThat(test2.getUsername(), is("UserServiceTest"));
            assertThat(test2.getPassword(), not("UserServiceTest"));
            assertThat(test2.getFullName(), is("Test UserServiceTest"));
            assertThat(test2.isAdmin(), is(false));
        } finally {
            userService.markForDelete(username);
            assertThat(userService.exists(username), is(false));

            // remove user for real so test can repeat
            userDAO.delete(username);
        }
    }

    @Test
    public void createTestUsers() {
        if (!userService.exists("test")) {
            User user = createUser("test", "test", "Dev Tester");
            user.setAdmin(true);
            userService.create(user);

            user = createUser("test2", "test", "Dev2 Tester");
            userService.create(user);
            user = createUser("test3", "test", "Dev3 Tester");
            userService.create(user);
            user = createUser("test4", "test", "Dev4 Tester");
            userService.create(user);
        }
    }

}