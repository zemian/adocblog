package com.zemian.adocblog.data.dao;

import com.zemian.adocblog.data.domain.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class ContentDAO {
    private static Logger LOG = LoggerFactory.getLogger(ContentDAO.class);

    @Autowired
    private JdbcTemplate jdbc;

    /*
    Map all columns except the content_text.
     */
    public static class ContentMetaRowMapper implements RowMapper<Content> {
        @Override
        public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
            return mapRow(rs, rowNum, "");
        }

        public Content mapRow(ResultSet rs, int rowNum, String prefix) throws SQLException {
            Content content = new Content();
            content.setContentId(rs.getInt(prefix + "content_id"));
            content.setTitle(rs.getString(prefix + "title"));
            content.setVersion(rs.getInt(prefix + "version"));
            content.setReasonForEdit(rs.getString(prefix + "reason_for_edit"));
            content.setCreatedUser(rs.getString(prefix + "created_user"));
            content.setCreatedDt(rs.getTimestamp(prefix + "created_dt").toLocalDateTime());
            content.setFormat(Content.Format.valueOf(rs.getString(prefix + "format")));

            return content;
        }
    }

    /*
    Full content record mapping including content_text
     */
    public static class ContentRowMapper implements RowMapper<Content> {
        private ContentMetaRowMapper contentMetaRowMapper = new ContentMetaRowMapper();
        @Override
        public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
            Content content = contentMetaRowMapper.mapRow(rs, rowNum);
            content.setContentText(rs.getString("content_text"));
            return content;
        }
    }

    public void create(Content content) {
        String sql = "INSERT INTO contents(" +
                " title, content_text, version, reason_for_edit, created_user, created_dt, format)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int ret = jdbc.update((conn) -> {
            int idx = 1;
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(idx++, content.getTitle());
            pstmt.setString(idx++, content.getContentText());
            pstmt.setInt(idx++, content.getVersion());
            pstmt.setString(idx++, content.getReasonForEdit());
            pstmt.setString(idx++, content.getCreatedUser());
            pstmt.setTimestamp(idx++, Timestamp.valueOf(content.getCreatedDt()));
            pstmt.setString(idx++, content.getFormat().name());
            return pstmt;
        }, keyHolder);
        content.setContentId((Integer)keyHolder.getKeys().get("content_id"));
        LOG.info("Inserted contents.content_id={} result: {}", content.getContentId(), ret);
    }

    /*
    Get full content with content text
     */
    public Content get(Integer contentId) {
        String sql = "SELECT * FROM contents WHERE content_id = ?";
        return jdbc.queryForObject(sql, new ContentRowMapper(), contentId);
    }

    /*
    Get Content meta only (no content text)
     */
    public Content getContentMeta(Integer contentId) {
        String sql = "SELECT * FROM contents WHERE content_id = ?";
        return jdbc.queryForObject(sql, new ContentMetaRowMapper(), contentId);
    }

    /*
    Get the content text only
     */
    public String getContentText(Integer contentId) {
        String sql = "SELECT content_text FROM contents WHERE content_id = ?";
        return jdbc.queryForObject(sql, String.class, contentId);
    }

    /*
    Delete a content record.
     */
    public int delete(Integer contentId) {
        String sql = "DELETE FROM contents WHERE content_id = ?";
        int ret = jdbc.update(sql, contentId);
        LOG.info("Deleted contents.content_id={} result: {}", contentId, ret);
        return ret;
    }
}
