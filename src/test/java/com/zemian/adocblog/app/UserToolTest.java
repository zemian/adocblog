package com.zemian.adocblog.app;

import com.zemian.adocblog.SpringTestBase;
import com.zemian.adocblog.data.DataConfig;
import com.zemian.adocblog.data.dao.UserDAO;
import com.zemian.adocblog.data.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@ContextConfiguration(classes = DataConfig.class)
public class UserToolTest extends SpringTestBase {
    @Autowired
    private UserDAO userDAO;

    @Test
    public void createNormalUser() {
        String username = "UserToolTest";
        UserTool.main(new String[]{"--create", username, "test"});
        User user = userDAO.get(username);
        try {
            assertThat(user.getUsername(), is(username));
            assertThat(user.isAdmin(), is(false));
            assertThat(user.getFullName(), is("User " + username));
            assertThat(user.getPassword(), not("test"));
        } finally {
            userDAO.delete(username);
        }
    }

    @Test
    public void createNormalUserWithFullName() {
        String username = "UserToolTest";
        UserTool.main(new String[]{"--create", "--fullName=Zemian Deng", username, "test"});
        User user = userDAO.get(username);
        try {
            assertThat(user.getUsername(), is(username));
            assertThat(user.isAdmin(), is(false));
            assertThat(user.getFullName(), is("Zemian Deng"));
            assertThat(user.getPassword(), not("test"));
        } finally {
            userDAO.delete(username);
        }
    }

    @Test
    public void createAdminUserWithFullName() {
        String username = "UserToolTest";
        UserTool.main(new String[]{"--create", "--adminUser=true", "--fullName=Zemian Deng", username, "test"});
        User user = userDAO.get(username);
        try {
            assertThat(user.getUsername(), is(username));
            assertThat(user.isAdmin(), is(true));
            assertThat(user.getFullName(), is("Zemian Deng"));
            assertThat(user.getPassword(), not("test"));
        } finally {
            userDAO.delete(username);
        }
    }
}
