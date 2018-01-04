package com.zemian.adocblog.data.dao;

import com.zemian.adocblog.data.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * A DAO to access User domain.
 */
@Repository
public class UserDAO extends AbstractDAO {
    public static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User ret = new User();
            ret.setUsername((String) rs.getObject("username"));
            ret.setPassword((String) rs.getObject("password"));
            ret.setFullName((String) rs.getObject("full_name"));
            ret.setAdmin((Boolean) rs.getObject("admin"));
            ret.setDeleted((Boolean) rs.getObject("deleted"));
            ret.setCreatedDt(rs.getTimestamp("created_dt").toLocalDateTime());
            return ret;
        }
    }

    private static Logger LOG = LoggerFactory.getLogger(UserDAO.class);

    public void create(User user) {
        String sql = "INSERT INTO users(username, password, full_name, admin) VALUES(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int ret = jdbc.update((conn) -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            int idx = 1;
            pstmt.setObject(idx++, user.getUsername());
            pstmt.setObject(idx++, user.getPassword());
            pstmt.setObject(idx++, user.getFullName());
            pstmt.setObject(idx++, user.isAdmin());
            return pstmt;
        }, keyHolder);

        // Retrieve and save the generate key
        user.setUsername((String)keyHolder.getKeys().get("username"));
        LOG.info("Inserted {}, result={}", user, ret);
    }

    /** Update user but not password */
    public void update(User user) {
        String sql = "UPDATE users SET" +
            " full_name = ?," +
            " admin = ?" +
            " WHERE username = ?";
        int ret = jdbc.update(sql,
            user.getFullName(),
            user.isAdmin(),
            user.getUsername());
        LOG.debug("Updated {}, result={}", user, ret);
    }

    public User get(String username) {
        String sql = "SELECT * FROM users WHERE username = ? AND deleted = FALSE";
        return jdbc.queryForObject(sql, new UserRowMapper(), username);
    }

    public void delete(String username) {
        int ret = jdbc.update("DELETE FROM users WHERE username = ?", username);
        LOG.debug("Deleted User username={}, result={}", username, ret);
    }

    public boolean exists(String username) {
        String sql = "SELECT EXISTS(SELECT username FROM users WHERE username = ? AND deleted = FALSE)";
        return jdbc.queryForObject(sql, Boolean.class, username);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users WHERE deleted = FALSE ORDER BY username";
        return jdbc.query(sql, new UserRowMapper());
    }

    public PagingList<User> find(Paging paging) {
        String sql = "SELECT * FROM users WHERE deleted = FALSE ORDER BY username";
        return findByPaging(sql, new UserRowMapper(), paging);
    }

    // == Customize

    public void markForDelete(String username) {
        String sql = "UPDATE users SET deleted = TRUE WHERE username = ?";
        int ret = jdbc.update(sql, username);
        LOG.debug("User username={} marked for delete. result={}", username, ret);
    }

    public void changePassword(String username, String password) {
        String sql = "UPDATE users SET" +
                " password = ?" +
                " WHERE username = ?";
        int ret = jdbc.update(sql,
                password,
                username);
        LOG.debug("Password updated for {}, result={}", username, ret);
    }
}
