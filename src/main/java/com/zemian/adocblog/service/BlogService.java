package com.zemian.adocblog.service;

import com.zemian.adocblog.data.dao.BlogDAO;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * A Doc that contains one or more Content data.
 */
@Service
@Transactional
public class BlogService extends DocService {

    @Autowired
    private BlogDAO blogDAO;

    // Override Search Methods that auto set doc type to BLOG
    public PagingList<Doc> findLatest(Paging paging) {
        return docDAO.findLatest(paging, Doc.Type.BLOG);
    }

    public PagingList<Doc> findPublished(Paging paging) {
        return docDAO.findPublished(paging, Doc.Type.BLOG);
    }

    public PagingList<Doc> searchPublished(Paging paging, String searchTerms) {
        return docDAO.searchPublished(paging, Doc.Type.BLOG, searchTerms);
    }

    public Doc getPrevBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        return blogDAO.getPrevBlog(currentBlogId, publishedDt);
    }

    public Doc getNextBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        return blogDAO.getNextBlog(currentBlogId, publishedDt);
    }
}
