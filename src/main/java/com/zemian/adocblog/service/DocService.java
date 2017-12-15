package com.zemian.adocblog.service;

import com.zemian.adocblog.data.dao.DocDAO;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.domain.DocHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Doc that contains one or more Content data.
 */
@Service
@Transactional
public class DocService {

    @Autowired
    protected DocDAO docDAO;

    public void create(Doc doc) {
        docDAO.create(doc);
    }

    // ==
    public void markForDelete(Integer docId, String reasonForDelete) {
        docDAO.markForDelete(docId, reasonForDelete);
    }

    public void delete(Integer docId) {
        docDAO.delete(docId);
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
        doc.getLatestContent().setVersion(doc.getLatestContent().getVersion() + 1);
        docDAO.update(doc);
    }

    public void publish(Doc doc) {
        docDAO.publish(doc);
    }

    public void unpublish(Integer docId) {
        docDAO.unpublish(docId);
    }

    // ==
    public PagingList<Doc> findLatest(Paging paging, Doc.Type type) {
        return docDAO.findLatest(paging, type);
    }

    public PagingList<Doc> findPublished(Paging paging, Doc.Type type) {
        return docDAO.findPublished(paging, type);
    }

    public PagingList<Doc> searchPublished(Paging paging, Doc.Type type, String searchTerms) {
        return docDAO.searchPublished(paging, type, searchTerms);
    }
}
