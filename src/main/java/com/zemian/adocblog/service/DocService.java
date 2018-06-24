package com.zemian.adocblog.service;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.data.dao.AuditLogDAO;
import com.zemian.adocblog.data.dao.DocDAO;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.AuditLog;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.domain.DocHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A Doc that contains one or more Content data.
 */
@Service
@Transactional
public class DocService {

    @Autowired
    protected DocDAO docDAO;

    @Autowired
    protected AuditLogDAO auditLogDAO;

    public void create(Doc doc) {
        docDAO.create(doc);
        auditLogDAO.create("DOC_CREATED",
                "DocId=" + doc.getDocId() +
                        ", date=" + doc.getLatestContent().getCreatedDt() +
                        ", user=" + doc.getLatestContent().getCreatedUser());
    }

    // ==
    public void markForDelete(Integer docId, String reasonForDelete) {
        docDAO.markForDelete(docId, reasonForDelete);
        auditLogDAO.create("DOC_MARKED_FOR_DELETE",
                "DocId=" + docId +
                        ", reasonForDelete=" + reasonForDelete);
    }

    public void delete(Integer docId) {
        docDAO.delete(docId);
        auditLogDAO.create("DOC_DELETED", "DocId=" + docId);
    }

    // ==
    public Doc get(Integer id) {
        return docDAO.get(id);
    }

    public Doc getByPath(String path) {
        return docDAO.getByPath(path);
    }

    public DocHistory getDocHistory(Integer docId) {
        return docDAO.getDocHistory(docId);
    }

    public int getPublishedCount() {
        return docDAO.getPublishedCount();
    }

    public int getTotalCount() {
        return docDAO.getTotalCount();
    }

    // ==
    public void update(Doc doc) {
        // Auto update version before update doc
        doc.getLatestContent().setVersion(doc.getLatestContent().getVersion() + 1);
        docDAO.update(doc);
        auditLogDAO.create("DOC_UPDATED",
                "DocId=" + doc.getDocId() + ", date=" + doc.getLatestContent().getCreatedDt() +
                        ", contentId=" + doc.getLatestContent().getContentId() +
                        ", version=" + doc.getLatestContent().getVersion());
    }

    public void publish(Doc doc) {
        if (doc.getPublishedDt() == null) {
            throw new AppException("publishedDt is not set.");
        }
        if (doc.getPublishedUser() == null) {
            throw new AppException("publishedUser is not set.");
        }
        if (doc.getLatestContent() == null) {
            throw new AppException("publishedContent is not set.");
        }
        docDAO.publish(doc);
        auditLogDAO.create("DOC_PUBLISHED",
                "DocId=" + doc.getDocId() + ", publishedDt=" + doc.getPublishedDt() +
                        ", publishedUser=" + doc.getPublishedUser() +
                        ", contentId=" + doc.getPublishedContent().getContentId());
    }

    public void unpublish(Integer docId) {
        docDAO.unpublish(docId);
        auditLogDAO.create("DOC_UNPUBLISHED","DocId=" + docId);
    }

    // ==
    public PagingList<Doc> findLatest(Paging paging, Doc.Type type) {
        return docDAO.findLatest(paging, type);
    }

    public PagingList<Doc> findPublished(Paging paging, Doc.Type type) {
        return docDAO.findPublished(paging, type);
    }

    public PagingList<Doc> findPublishedByTags(Paging paging, Doc.Type type, String tags) {
        return docDAO.findPublishedByTags(paging, type, tags);
    }

    public PagingList<Doc> findPublishedByDate(Paging paging, Doc.Type type, LocalDateTime from, LocalDateTime to) {
        return docDAO.findPublishedByDate(paging, type, from, to);
    }

    public PagingList<Doc> searchPublished(Paging paging, Doc.Type type, String searchTerms) {
        return docDAO.searchPublished(paging, type, searchTerms);
    }

    public List<String> findAllExpandedTags() {
        List<String> tags = docDAO.findAllTags(Doc.Type.BLOG);
        Set<String> expandedTags = tags.stream().
                flatMap(e -> Arrays.stream(e.split("\\s+"))).
                collect(Collectors.toSet());
        List<String> ret = new ArrayList<>();
        ret.addAll(expandedTags);
        Collections.sort(ret);
        return ret;
    }

    public int removeOldDocs(LocalDateTime sinceDt) {
        return docDAO.removeOldDocs(sinceDt);
    }
}
