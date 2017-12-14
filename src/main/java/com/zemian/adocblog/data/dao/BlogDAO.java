package com.zemian.adocblog.data.dao;

import com.zemian.adocblog.data.domain.Blog;
import com.zemian.adocblog.data.domain.BlogHistory;
import com.zemian.adocblog.data.domain.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A Blog is a Blog with type = BLOG.
 */
@Repository
public class BlogDAO extends AbstractDAO {
    private static Logger LOG = LoggerFactory.getLogger(BlogDAO.class);

    @Autowired
    private ContentDAO contentDAO;

    public static final String SELECT_BLOGS_SQL = "SELECT" +
            "   blogs.blog_id, blogs.published_user, blogs.published_dt," +
            "   latest_contents.content_id latest_content_id," +
            "   latest_contents.title latest_title," +
            "   latest_contents.version latest_version," +
            "   latest_contents.reason_for_edit latest_reason_for_edit," +
            "   latest_contents.created_user latest_created_user," +
            "   latest_contents.created_dt latest_created_dt," +
            "   latest_contents.format latest_format," +
            "   latest_users.full_name latest_full_name," +
            "   published_contents.content_id published_content_id," +
            "   published_contents.title published_title," +
            "   published_contents.version published_version," +
            "   published_contents.reason_for_edit published_reason_for_edit," +
            "   published_contents.created_user published_created_user," +
            "   published_contents.created_dt published_created_dt," +
            "   published_contents.format published_format," +
            "   published_users.full_name published_full_name" +
            " FROM blogs" +
            "   LEFT JOIN contents latest_contents ON latest_contents.content_id = blogs.latest_content_id" +
            "   LEFT JOIN users latest_users ON latest_users.username = latest_contents.created_user" +
            "   LEFT JOIN contents published_contents ON published_contents.content_id = blogs.published_content_id" +
            "   LEFT JOIN users published_users ON published_users.username = published_contents.created_user";

    /*
    This query find all latest and published blogs with content meta info.
     */
    public static final String SELECT_LATEST_BLOGS_SQL = SELECT_BLOGS_SQL +
            " WHERE blogs.deleted = FALSE AND blogs.latest_content_id IS NOT NULL";

    /*
    This query find all published only blogs with content meta info.
     */
    public static final String SELECT_PUBLISHED_BLOGS_SQL =  SELECT_BLOGS_SQL +
            " WHERE blogs.deleted = FALSE AND blogs.latest_content_id IS NOT NULL" +
            "   AND blogs.published_content_id IS NOT NULL";

    public int getPublishedCount() {
        String sql = "SELECT COUNT(*) FROM blogs WHERE deleted = FALSE AND published_content_id IS NOT NULL";
        return jdbc.queryForObject(sql, Integer.class);
    }

    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM blogs WHERE deleted = FALSE";
        return jdbc.queryForObject(sql, Integer.class);
    }

    public static class BlogRowMapper implements RowMapper<Blog> {
        private ContentDAO.ContentMetaRowMapper contentMetaRowMapper = new ContentDAO.ContentMetaRowMapper();
        @Override
        public Blog mapRow(ResultSet rs, int rowNum) throws SQLException {
            Blog blog = new Blog();
            blog.setBlogId(rs.getInt("blog_id"));

            Content latestContent = contentMetaRowMapper.mapRow(rs, rowNum, "latest_");
            blog.setLatestContent(latestContent);

            Integer publishedContentId = (Integer)rs.getObject("published_content_id");
            if (publishedContentId != null) {

                blog.setPublishedUser(rs.getString("published_user"));
                blog.setPublishedDt(rs.getTimestamp("published_dt").toLocalDateTime());

                if (publishedContentId == latestContent.getContentId()) {
                    blog.setPublishedContent(latestContent);
                } else {
                    Content publishedContent = contentMetaRowMapper.mapRow(rs, rowNum, "published_");
                    blog.setPublishedContent(publishedContent);
                }
            }

            return blog;
        }
    }

    private void createContentVer(Integer blogId, Integer contentId) {
        String sql = "INSERT INTO content_vers(blog_id, content_id) VALUES(?, ?)";
        int ret = jdbc.update(sql, blogId, contentId);
        LOG.debug("Inserted content_vers for blogId={} and contentId={}. Ret={}", blogId, contentId, ret);

    }

    public void create(Blog blog) {
        contentDAO.create(blog.getLatestContent());

        final String sql2 = "INSERT INTO blogs(latest_content_id) VALUES(?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int ret = jdbc.update((conn) -> {
            int idx = 1;
            PreparedStatement pstmt = conn.prepareStatement(sql2, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(idx++, blog.getLatestContent().getContentId());
            return pstmt;
        }, keyHolder);
        blog.setBlogId((Integer)keyHolder.getKeys().get("blog_id"));

        createContentVer(blog.getBlogId(), blog.getLatestContent().getContentId());

        LOG.info("Inserted blogs.blog_id={} with contents.content_id={} result: {}",
                blog.getBlogId(), blog.getLatestContent().getContentId(), ret);
    }

    public void update(Blog blog) {
        contentDAO.create(blog.getLatestContent());
        createContentVer(blog.getBlogId(), blog.getLatestContent().getContentId());

        final String sql = "UPDATE blogs SET latest_content_id = ? WHERE blog_id = ?";
        int ret = jdbc.update(sql, blog.getLatestContent().getContentId(), blog.getBlogId());
        LOG.info("Updated blogId={} with contentId={}. result: {}",
                blog.getBlogId(), blog.getLatestContent().getContentId(), ret);
    }

    public Blog get(Integer id) {
        String sql = SELECT_LATEST_BLOGS_SQL + " AND blogs.blog_id = ?";
        return jdbc.queryForObject(sql, new BlogRowMapper(), id);
    }

    public BlogHistory getBlogHistory(Integer blogId) {
        String sql = "SELECT contents.* FROM contents" +
                " LEFT JOIN content_vers ON content_vers.content_id = contents.content_id" +
                " WHERE content_vers.blog_id = ? ORDER BY contents.version DESC";

        ContentDAO.ContentMetaRowMapper contentMetaRowMapper = new ContentDAO.ContentMetaRowMapper();
        List<Content> contentVers = jdbc.query(sql, contentMetaRowMapper, blogId);
        BlogHistory bh = new BlogHistory();
        bh.setContentVers(contentVers);
        bh.setBlogId(blogId);

        sql = "SELECT published_content_id FROM blogs WHERE blog_id = ?";
        Integer publishedContentId = jdbc.queryForObject(sql, Integer.class, blogId);
        bh.setPublishedContentId(publishedContentId);

        LOG.debug("Found {} content versions for blogId={}.", bh.getContentVers().size(), blogId);
        return bh;
    }

    public void markForDelete(Integer blogId, String reasonForDelete) {
        int ret = jdbc.update("UPDATE blogs SET deleted = TRUE, reason_for_delete = ? WHERE blog_id = ?",
                reasonForDelete, blogId);
        LOG.debug("BlogId={} marked for markForDelete. result={}", blogId, ret);
    }

    /*
    Delete a blog AND all of related table records!
     */
    public void delete(Integer blogId) {
        String sql;
        int ret;

        // Get list of content ids to be markForDelete
        sql = "SELECT content_id FROM content_vers WHERE blog_id = ?";
        List<Integer> contentIds = jdbc.queryForList(sql, Integer.class, blogId);

        // Delete content vers
        sql = "DELETE FROM content_vers WHERE blog_id = ?";
        ret = jdbc.update(sql, blogId);
        LOG.debug("Deleted {} content_vers with blogId={}", ret, blogId);

        // Delete blog
        sql = "DELETE FROM blogs WHERE blog_id = ?";
        ret = jdbc.update(sql, blogId);
        LOG.info("Deleted blogs.blog_id={} result: {}", blogId, ret);

        // Delete contents. Let's do the long way for now.
        for (Integer contentId : contentIds) {
            contentDAO.delete(contentId);
        }
        LOG.debug("Deleted {} contents with blogId={}", contentIds.size(), blogId);
    }

    public PagingList<Blog> findLatest(Paging paging) {
        String sql = SELECT_LATEST_BLOGS_SQL + " ORDER BY latest_contents.created_dt DESC";
        PagingList<Blog> ret = findByPaging(sql, new BlogRowMapper(), paging);
        LOG.debug("Found {} blogs.", ret);
        return ret;
    }

    public PagingList<Blog> findPublished(Paging paging) {
        String sql = SELECT_PUBLISHED_BLOGS_SQL + " ORDER BY blogs.published_dt DESC";
        PagingList<Blog> ret = findByPaging(sql, new BlogRowMapper(), paging);
        LOG.debug("Found {} published blogs.", ret);
        return ret;
    }

    public PagingList<Blog> searchPublished(Paging paging, String searchTerms) {
        String sql = SELECT_PUBLISHED_BLOGS_SQL +
                " AND to_tsvector(blogs.title || ' ' || contents.content_text) @@ to_tsquery(?)" +
                " ORDER BY blogs.published_dt DESC";
        PagingList<Blog> ret = findByPaging(sql, new BlogRowMapper(), paging, searchTerms);
        LOG.debug("Found {} published blogs with full text search.", ret);
        return ret;
    }

    public void publish(Blog blog) {
        Integer blogId = blog.getBlogId();
        Integer contentId = blog.getLatestContent().getContentId();
        String username = blog.getPublishedUser();

        String sql = "UPDATE blogs SET published_content_id = ?," +
                " published_user = ?," +
                " published_dt = ? WHERE blog_id = ?";
        int ret = jdbc.update(sql, contentId, username, blog.getPublishedDt(), blogId);
        LOG.info("Published docId={} with contentId={}. ret={}", blogId, contentId, ret);
    }

    public void unpublish(Integer blogId) {
        String sql = "UPDATE blogs SET published_content_id = NULL," +
                " published_user = NULL, published_dt = NULL WHERE blog_id = ?";
        int ret = jdbc.update(sql, blogId);
        LOG.info("Unpublished docId={} ret={}", blogId, ret);
    }

    /*
    Get previous published blog based on given blog. Return null if not found.
     */
    public Blog findPrevBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        String sql = SELECT_PUBLISHED_BLOGS_SQL + " AND blogs.published_dt > ? AND blogs.blog_id <> ?" +
                " ORDER BY blogs.published_dt ASC LIMIT 1";
        return findBlogByPublishDt(sql, publishedDt, currentBlogId);
    }

    /*
    Get next published blog based on given blog. Return null if not found.
     */
    public Blog findNextBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        String sql = SELECT_PUBLISHED_BLOGS_SQL + " AND blogs.published_dt < ? AND blogs.blog_id <> ?" +
                " ORDER BY blogs.published_dt DESC LIMIT 1";
        return findBlogByPublishDt(sql, publishedDt, currentBlogId);
    }

    /*
    Support find Prev/Next Blog that takes a SQL to query max of one Blog object.
     */
    private Blog findBlogByPublishDt(String sql, LocalDateTime publishDt, Integer blogId) {
        List<Blog> list = jdbc.query(sql, new BlogRowMapper(), publishDt, blogId);
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }
}
