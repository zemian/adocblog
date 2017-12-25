package com.zemian.adocblog.data.dao;

import com.zemian.adocblog.data.domain.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SettingDAO extends AbstractDAO {

    private static Logger LOG = LoggerFactory.getLogger(SettingDAO.class);

    public static class SettingRowMapper implements RowMapper<Setting> {
        @Override
        public Setting mapRow(ResultSet rs, int rowNum) throws SQLException {
            Setting ret = new Setting();
            ret.setSettingId(rs.getInt("setting_id"));
            ret.setCategory(rs.getString("category"));
            ret.setName(rs.getString("name"));
            ret.setValue(rs.getString("value"));
            ret.setDescription(rs.getString("description"));
            ret.setType(Setting.Type.valueOf(rs.getString("type")));
            return ret;
        }
    }

    /*
    Save setting object into table and set the generated ID value.
     */
    public void create(Setting setting) {
        String sql = "INSERT INTO settings(category, name, value, type, description) VALUES(?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update((conn) -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, setting.getCategory());
            pstmt.setString(2, setting.getName());
            pstmt.setString(3, setting.getValue());
            pstmt.setString(4, setting.getType().name());
            pstmt.setString(5, setting.getDescription());
            return pstmt;
        }, keyHolder);

        // Retrieve and save generate setting_id
        setting.setSettingId((Integer)keyHolder.getKeys().get("setting_id"));
        LOG.debug("Created setting_id={}", setting.getSettingId());
    }

    public Setting get(Integer settingId) {
        String sql = "SELECT * FROM settings WHERE setting_id = ?";
        return jdbc.queryForObject(sql, new SettingRowMapper(), settingId);
    }

    public void delete(Integer settingId) {
        String sql = "DELETE FROM settings WHERE setting_id = ?";
        int ret = jdbc.update(sql, settingId);
        LOG.debug("Deleted setting_id={} result={}", settingId, ret);
    }

    public void update(Setting setting) {
        String sql = "UPDATE settings SET" +
                " category = ?," +
                " name = ?," +
                " value = ?," +
                " type = ?," +
                " description = ?" +
                " WHERE setting_id = ?";
        int ret = jdbc.update(sql,
                setting.getCategory(),
                setting.getName(),
                setting.getValue(),
                setting.getType(),
                setting.getDescription(),
                setting.getSettingId());
        LOG.debug("Updated setting_id={} result={}", setting.getSettingId(), ret);
    }

    public PagingList<Setting> find(Paging paging) {
        String sql = "SELECT * FROM settings ORDER BY category, name";
        PagingList<Setting> ret = findByPaging(sql, new SettingRowMapper(), paging);
        LOG.debug("Found {} Settings.", ret);
        return ret;
    }

    public List<Setting> findByCategory(String category) {
        String sql = "SELECT * FROM settings WHERE category = ? ORDER BY name";
        List<Setting> ret = jdbc.query(sql, new SettingRowMapper(), category);
        LOG.debug("Found {} Settings with category={}.", ret.size(), category);
        return ret;
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM settings";
        int totalCount = jdbc.queryForObject(sql, Number.class).intValue();
        return totalCount;
    }

    public boolean exists(String category, String name) {
        String sql = "SELECT EXISTS(SELECT name FROM settings WHERE category = ? AND name = ?)";
        return jdbc.queryForObject(sql, Boolean.class, category, name);
    }
}
