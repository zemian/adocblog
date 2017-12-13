package com.zemian.adocblog.data.dao;

import com.zemian.adocblog.data.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
    private static Logger LOG = LoggerFactory.getLogger(UserDAO.class);

    @Autowired
    private JdbcTemplate jdbc;

    public void create(User user) {
        int ret = jdbc.update("INSERT INTO users(username, password, full_name, is_admin) VALUES(?, ?, ?, ?)",
                user.getUsername(),
                user.getPassword(),
                user.getFullName(),
                user.isAdmin());
        LOG.info("Insert user {} result: {}", user.getUsername(), ret);
    }

    public User get(String username) {
        return jdbc.queryForObject("SELECT * FROM users WHERE username = ? AND deleted = FALSE", (rs, idx) -> {
            User ret = new User();
            ret.setUsername(rs.getString("username"));
            ret.setPassword(rs.getString("password"));
            ret.setFullName(rs.getString("full_name"));
            ret.setAdmin(rs.getBoolean("is_admin"));
            return ret;
        }, username);
    }

    public void markForDelete(String username) {
        int ret = jdbc.update("UPDATE users SET deleted = TRUE WHERE username = ?", username);
        LOG.debug("User username={} marked for markForDelete. result={}", username, ret);
    }

    public void delete(String username) {
        int ret = jdbc.update("DELETE FROM users WHERE username = ?", username);
        LOG.debug("User username={} deleted result={}", username, ret);
    }

    public boolean exists(String username) {
        String sql = "SELECT EXISTS(SELECT username FROM users WHERE username = ? AND deleted = FALSE)";
        return jdbc.queryForObject(sql, Boolean.class, username);
    }
}
