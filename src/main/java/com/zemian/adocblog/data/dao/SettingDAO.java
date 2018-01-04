package com.zemian.adocblog.data.dao;

import com.zemian.adocblog.data.domain.Setting;
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
 * A DAO to access Setting domain.
 *
 * This class is generated by Zemian's CodeGen Toolbox on Jan 1, 2018.
 */
@Repository
public class SettingDAO extends AbstractDAO {
    public static class SettingRowMapper implements RowMapper<Setting> {
        @Override
        public Setting mapRow(ResultSet rs, int rowNum) throws SQLException {
            Setting ret = new Setting();
            ret.setSettingId((Integer) rs.getObject("setting_id"));
            ret.setCategory((String) rs.getObject("category"));
            ret.setName((String) rs.getObject("name"));
            ret.setValue((String) rs.getObject("value"));
            ret.setType(Setting.Type.valueOf(rs.getString("type")));
            ret.setDescription((String) rs.getObject("description"));
            return ret;
        }
    }

    private static Logger LOG = LoggerFactory.getLogger(SettingDAO.class);

    public void create(Setting setting) {
        String sql = "INSERT INTO settings(category, name, value, type, description) VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int ret = jdbc.update((conn) -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            int idx = 1;
            pstmt.setObject(idx++, setting.getCategory());
            pstmt.setObject(idx++, setting.getName());
            pstmt.setObject(idx++, setting.getValue());
            pstmt.setObject(idx++, setting.getType().name());
            pstmt.setObject(idx++, setting.getDescription());
            return pstmt;
        }, keyHolder);

        // Retrieve and save the generate key
        setting.setSettingId((Integer)keyHolder.getKeys().get("setting_id"));
        LOG.info("Inserted {}, result={}", setting, ret);
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
            setting.getType().name() ,
            setting.getDescription(),
            setting.getSettingId());
        LOG.debug("Updated {}, result={}", setting, ret);
    }

    public Setting get(Integer settingId) {
        String sql = "SELECT * FROM settings WHERE setting_id = ?";
        return jdbc.queryForObject(sql, new SettingRowMapper(), settingId);
    }

    public void delete(Integer settingId) {
        int ret = jdbc.update("DELETE FROM settings WHERE setting_id = ?", settingId);
        LOG.debug("Deleted Setting settingId={}, result={}", settingId, ret);
    }

    public boolean exists(Integer settingId) {
        String sql = "SELECT EXISTS(SELECT setting_id FROM settings WHERE setting_id = ?)";
        return jdbc.queryForObject(sql, Boolean.class, settingId);
    }

    public List<Setting> findAll() {
        String sql = "SELECT * FROM settings ORDER BY setting_id";
        return jdbc.query(sql, new SettingRowMapper());
    }

    public PagingList<Setting> find(Paging paging) {
        String sql = "SELECT * FROM settings ORDER BY setting_id";
        return findByPaging(sql, new SettingRowMapper(), paging);
    }

    public List<Setting> findByCategory(String category) {
        String sql = "SELECT * FROM settings WHERE category = ? ORDER BY name";
        List<Setting> ret = jdbc.query(sql, new SettingRowMapper(), category);
        LOG.debug("Found {} Settings with category={}.", ret.size(), category);
        return ret;
    }

    // == Customize

    public boolean exists(String category, String name) {
        String sql = "SELECT EXISTS(SELECT setting_id FROM settings WHERE category = ? AND name = ?)";
        return jdbc.queryForObject(sql, Boolean.class, category, name);
    }
}
