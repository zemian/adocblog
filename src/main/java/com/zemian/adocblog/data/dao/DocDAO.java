package com.zemian.adocblog.data.dao;

import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.domain.DocHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * A Doc contains one or more Content data.
 */
@Repository
public class DocDAO extends AbstractDAO {
    private static Logger LOG = LoggerFactory.getLogger(DocDAO.class);

    @Autowired
    protected ContentDAO contentDAO;

    /* Base query to select docs with content meta info (without actual content_text) */
    public static final String SELECT_DOCS_SQL = "SELECT" +
            "   docs.doc_id," +
            "   docs.path," +
            "   docs.type," +
            "   docs.published_user," +
            "   docs.published_dt," +
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
            " FROM docs" +
            "   LEFT JOIN contents latest_contents ON latest_contents.content_id = docs.latest_content_id" +
            "   LEFT JOIN users latest_users ON latest_users.username = latest_contents.created_user" +
            "   LEFT JOIN contents published_contents ON published_contents.content_id = docs.published_content_id" +
            "   LEFT JOIN users published_users ON published_users.username = published_contents.created_user";

    /*
    This query find all latest and published docs.
     */
    public static final String SELECT_LATEST_DOCS_SQL = SELECT_DOCS_SQL +
            " WHERE docs.deleted = FALSE AND docs.latest_content_id IS NOT NULL";

    /*
    This query find all published only docs.
     */
    public static final String SELECT_PUBLISHED_DOCS_SQL =  SELECT_DOCS_SQL +
            " WHERE docs.deleted = FALSE AND docs.latest_content_id IS NOT NULL" +
            "   AND docs.published_content_id IS NOT NULL";

    /* A Doc with Content meta data ResultSet mapping. */
    public static class DocRowMapper implements RowMapper<Doc> {
        private ContentDAO.ContentMetaRowMapper contentMetaRowMapper = new ContentDAO.ContentMetaRowMapper();
        @Override
        public Doc mapRow(ResultSet rs, int rowNum) throws SQLException {
            Doc doc = new Doc();
            doc.setDocId(rs.getInt("doc_id"));
            doc.setPath(rs.getString("path"));
            doc.setType(Doc.Type.valueOf(rs.getString("type")));

            Content latestContent = contentMetaRowMapper.mapRow(rs, rowNum, "latest_");
            latestContent.setAuthorFullName(rs.getString("latest_full_name"));
            doc.setLatestContent(latestContent);

            Integer publishedContentId = (Integer)rs.getObject("published_content_id");
            if (publishedContentId != null) {

                doc.setPublishedUser(rs.getString("published_user"));
                doc.setPublishedDt(rs.getTimestamp("published_dt").toLocalDateTime());

                if (publishedContentId == latestContent.getContentId()) {
                    doc.setPublishedContent(latestContent);
                } else {
                    Content publishedContent = contentMetaRowMapper.mapRow(rs, rowNum, "published_");
                    publishedContent.setAuthorFullName(rs.getString("published_full_name"));
                    doc.setPublishedContent(publishedContent);
                }
            }

            return doc;
        }
    }

    // == DAO Create
    private void createDocContents(Integer docId, Integer contentId) {
        String sql = "INSERT INTO doc_contents(doc_id, content_id) VALUES(?, ?)";
        int ret = jdbc.update(sql, docId, contentId);
        LOG.debug("Inserted doc_contents for docId={} and contentId={}. Ret={}", docId, contentId, ret);

    }

    public void create(Doc doc) {
        // Insert content first
        contentDAO.create(doc.getLatestContent());

        // Generate new DocID
        String sql = "SELECT nextval('docs_doc_id_seq') new_doc_id";
        Integer newDocId = jdbc.queryForObject(sql, Integer.class);
        doc.setDocId(newDocId);
        if (doc.getPath() == null) {
            doc.setPath("" + newDocId);
        }

        // Insert doc
        sql = "INSERT INTO docs(doc_id, path, type, latest_content_id) VALUES(?, ?, ?, ?)";
        int ret = jdbc.update(sql,
            doc.getDocId(),
            doc.getPath(),
            doc.getType().name(),
            doc.getLatestContent().getContentId());

        // Insert doc content version link
        createDocContents(doc.getDocId(), doc.getLatestContent().getContentId());

        // Done
        LOG.info("Inserted {} result: {}", doc, ret);
    }

    // == DAO Delete
    public void markForDelete(Integer docId, String reasonForDelete) {
        int ret = jdbc.update("UPDATE docs SET deleted = TRUE, reason_for_delete = ? WHERE doc_id = ?",
                reasonForDelete, docId);
        LOG.debug("DocId={} marked for markForDelete. result={}", docId, ret);
    }

    /*
    Delete a doc AND all of related table records!
     */
    public void delete(Integer docId) {
        String sql;
        int ret;

        // Get list of content ids to be markForDelete
        sql = "SELECT content_id FROM doc_contents WHERE doc_id = ?";
        List<Integer> contentIds = jdbc.queryForList(sql, Integer.class, docId);

        // Delete content vers
        sql = "DELETE FROM doc_contents WHERE doc_id = ?";
        ret = jdbc.update(sql, docId);
        LOG.debug("Deleted {} doc_contents with docId={}", ret, docId);

        // Delete doc
        sql = "DELETE FROM docs WHERE doc_id = ?";
        ret = jdbc.update(sql, docId);
        LOG.info("Deleted docs.doc_id={} result: {}", docId, ret);

        // Delete contents. Let's do the long way for now.
        for (Integer contentId : contentIds) {
            contentDAO.delete(contentId);
        }
        LOG.debug("Deleted {} contents with docId={}", contentIds.size(), docId);
    }

    // == DAO Retrieval/Get
    public Doc get(Integer id) {
        String sql = SELECT_LATEST_DOCS_SQL + " AND docs.doc_id = ?";
        return jdbc.queryForObject(sql, new DocRowMapper(), id);
    }

    public Doc getByPath(String path) {
        String sql = SELECT_LATEST_DOCS_SQL + " AND docs.path = ?";
        return jdbc.queryForObject(sql, new DocRowMapper(), path);
    }

    public DocHistory getDocHistory(Integer docId) {
        String sql = "SELECT contents.* FROM contents" +
                " LEFT JOIN doc_contents ON doc_contents.content_id = contents.content_id" +
                " WHERE doc_contents.doc_id = ? ORDER BY contents.version DESC";

        ContentDAO.ContentMetaRowMapper contentMetaRowMapper = new ContentDAO.ContentMetaRowMapper();
        List<Content> contentVers = jdbc.query(sql, contentMetaRowMapper, docId);
        DocHistory bh = new DocHistory();
        bh.setContentVers(contentVers);
        bh.setDocId(docId);

        sql = "SELECT published_content_id FROM docs WHERE doc_id = ?";
        Integer publishedContentId = jdbc.queryForObject(sql, Integer.class, docId);
        bh.setPublishedContentId(publishedContentId);

        LOG.debug("Found {} content versions for docId={}.", bh.getContentVers().size(), docId);
        return bh;
    }

    public int getPublishedCount() {
        String sql = "SELECT COUNT(*) FROM docs WHERE deleted = FALSE AND published_content_id IS NOT NULL";
        return jdbc.queryForObject(sql, Integer.class);
    }

    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM docs WHERE deleted = FALSE";
        return jdbc.queryForObject(sql, Integer.class);
    }

    // == DAO Updates
    public void update(Doc doc) {
        contentDAO.create(doc.getLatestContent());
        createDocContents(doc.getDocId(), doc.getLatestContent().getContentId());

        String sql = "UPDATE docs SET" +
                " path = ?," +
                " type = ?," +
                " latest_content_id = ?" +
                " WHERE doc_id = ?";
        int ret = jdbc.update(sql,
                doc.getPath(),
                doc.getType().name(),
                doc.getLatestContent().getContentId(),
                doc.getDocId());

        LOG.info("Updated {} with new content. result: {}", doc, ret);
    }

    public void publish(Doc doc) {
        Integer docId = doc.getDocId();
        Integer contentId = doc.getLatestContent().getContentId();
        String username = doc.getPublishedUser();

        String sql = "UPDATE docs SET published_content_id = ?," +
                " published_user = ?," +
                " published_dt = ? WHERE doc_id = ?";
        int ret = jdbc.update(sql, contentId, username, doc.getPublishedDt(), docId);
        LOG.info("Published {}. ret={}", doc, ret);
    }

    public void unpublish(Integer docId) {
        String sql = "UPDATE docs SET published_content_id = NULL," +
                " published_user = NULL, published_dt = NULL WHERE doc_id = ?";
        int ret = jdbc.update(sql, docId);
        LOG.info("Unpublished docId={} ret={}", docId, ret);
    }

    // == DAO Find List and Searches
    public PagingList<Doc> findLatest(Paging paging, Doc.Type type) {
        String sql = SELECT_LATEST_DOCS_SQL + " AND docs.type = ? ORDER BY latest_contents.created_dt DESC";
        PagingList<Doc> ret = findByPaging(sql, new DocRowMapper(), paging, type.name());
        LOG.debug("Found {} docs.", ret);
        return ret;
    }

    public PagingList<Doc> findPublished(Paging paging, Doc.Type type) {
        String sql = SELECT_PUBLISHED_DOCS_SQL + " AND docs.type = ? ORDER BY docs.published_dt DESC";
        PagingList<Doc> ret = findByPaging(sql, new DocRowMapper(), paging, type.name());
        LOG.debug("Found {} published docs.", ret);
        return ret;
    }

    public PagingList<Doc> searchPublished(Paging paging, Doc.Type type, String fullTextQuery) {
        LOG.debug("Searching with fullTextQuery={}", fullTextQuery);
        String sql = SELECT_PUBLISHED_DOCS_SQL +
                " AND docs.type = ? AND" +
                " to_tsvector(published_contents.title || ' ' || published_contents.content_text) @@ to_tsquery(?)" +
                " ORDER BY docs.published_dt DESC";
        PagingList<Doc> ret = findByPaging(sql, new DocRowMapper(), paging, type.name(), fullTextQuery);
        LOG.debug("Found {} published docs with full text search.", ret);
        return ret;
    }
}
