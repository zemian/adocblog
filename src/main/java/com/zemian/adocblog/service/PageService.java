package com.zemian.adocblog.service;

import com.zemian.adocblog.data.dao.DocDAO;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * A Page is a Doc with type = PAGE.
 */
@Service
@Transactional
public class PageService extends DocService {
    @Autowired
    private DocDAO docDAO;

    // == Override parent methods to auto set doc type to BLOG
    public PagingList<Doc> findLatest(Paging paging) {
        return docDAO.findLatest(paging, Doc.Type.PAGE);
    }

    public PagingList<Doc> findPublished(Paging paging) {
        return docDAO.findPublished(paging, Doc.Type.PAGE);
    }

    public PagingList<Doc> searchPublished(Paging paging, String searchTerms) {
        return docDAO.searchPublished(paging, Doc.Type.PAGE, searchTerms);
    }
}
