package com.zemian.adocblog.data.dao;

import com.zemian.adocblog.data.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDAO extends AbstractDAO {
    private static Logger LOG = LoggerFactory.getLogger(UserDAO.class);

    public static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User ret = new User();
            ret.setUsername(rs.getString("username"));
            ret.setPassword(rs.getString("password"));
            ret.setFullName(rs.getString("full_name"));
            ret.setAdmin(rs.getBoolean("is_admin"));
            return ret;
        }
    }

    public void create(User user) {
        String sql = "INSERT INTO users(username, password, full_name, is_admin) VALUES(?, ?, ?, ?)";
        int ret = jdbc.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getFullName(),
                user.isAdmin());
        LOG.info("Insert user {} result: {}", user.getUsername(), ret);
    }

    public void update(User user) {
        String sql = "UPDATE users SET password = ?, full_name = ?, is_admin = ? WHERE username = ?";
        int ret = jdbc.update(sql,
                user.getPassword(),
                user.getFullName(),
                user.isAdmin(),
                user.getUsername());
        LOG.info("Updated user {} result: {}", user.getUsername(), ret);
    }

    public User get(String username) {
        String sql = "SELECT * FROM users WHERE username = ? AND deleted = FALSE";
        return jdbc.queryForObject(sql, new UserRowMapper(), username);
    }

    public void markForDelete(String username) {
        String sql = "UPDATE users SET deleted = TRUE WHERE username = ?";
        int ret = jdbc.update(sql, username);
        LOG.debug("User username={} marked for delete. result={}", username, ret);
    }

    public void delete(String username) {
        int ret = jdbc.update("DELETE FROM users WHERE username = ?", username);
        LOG.debug("User username={} deleted result={}", username, ret);
    }

    public PagingList<User> findAll(Paging paging) {
        String sql = "SELECT * FROM users ORDER BY username";
        return findByPaging(sql, new UserRowMapper(), paging);
    }

    public boolean exists(String username) {
        String sql = "SELECT EXISTS(SELECT username FROM users WHERE username = ? AND deleted = FALSE)";
        return jdbc.queryForObject(sql, Boolean.class, username);
    }
}
