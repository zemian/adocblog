package com.zemian.adocblog.data.dao;

import com.zemian.adocblog.data.domain.Doc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A Blog is a Doc with type = BLOG
 */
@Repository
public class BlogDAO extends DocDAO {
    private static Logger LOG = LoggerFactory.getLogger(BlogDAO.class);

    /*
    Get previous published blog based on given blogId. Return null if not found.
     */
    public Doc getPrevBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        String sql = SELECT_PUBLISHED_DOCS_SQL + " AND docs.type = ? AND docs.published_dt > ? AND docs.doc_id <> ?" +
                " ORDER BY docs.published_dt ASC LIMIT 1";
        return findBlogByPublishDt(sql, currentBlogId, publishedDt);
    }

    /*
    Get next published blog based on given blogId. Return null if not found.
     */
    public Doc getNextBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        String sql = SELECT_PUBLISHED_DOCS_SQL + " AND docs.type = ? AND docs.published_dt < ? AND docs.doc_id <> ?" +
                " ORDER BY docs.published_dt DESC LIMIT 1";
        return findBlogByPublishDt(sql, currentBlogId, publishedDt);
    }

    /*
    Support find Prev/Next Blog that takes a SQL to query max of one Doc object.
     */
    private Doc findBlogByPublishDt(String sql, Integer currentDocId, LocalDateTime publishedDt) {
        LOG.debug("Get blog by published date. Sql={}, currentDocId={}, publishedDt={}", sql, currentDocId, publishedDt);
        List<Doc> list = jdbc.query(sql, new DocRowMapper(), Doc.Type.BLOG.name(), publishedDt, currentDocId);
        if (list.size() <= 0) {
            LOG.debug("No blog found");
            return null;
        }
        Doc blog = list.get(0);
        LOG.debug("Found docId={}", blog.getDocId());
        return blog;
    }
}
