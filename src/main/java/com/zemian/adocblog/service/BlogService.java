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
 * A Blog is a Doc with type = BLOG.
 */
@Service
@Transactional
public class BlogService extends DocService {

    @Autowired
    private BlogDAO blogDAO;

    // == Override parent methods to auto set doc type to BLOG
    public PagingList<Doc> findLatest(Paging paging) {
        return docDAO.findLatest(paging, Doc.Type.BLOG);
    }

    public PagingList<Doc> findPublished(Paging paging) {
        return docDAO.findPublished(paging, Doc.Type.BLOG);
    }

    public PagingList<Doc> searchPublished(Paging paging, String searchTerms) {
        return docDAO.searchPublished(paging, Doc.Type.BLOG, searchTerms);
    }

    // == Blog specific methods
    public Doc getPrevBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        return blogDAO.getPrevBlog(currentBlogId, publishedDt);
    }

    public Doc getNextBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        return blogDAO.getNextBlog(currentBlogId, publishedDt);
    }
}
