package com.zemian.adocblog.service;

import com.zemian.adocblog.data.dao.BlogDAO;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Doc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    public PagingList<Doc> findPublishedByTags(Paging paging, String tags) {
        return super.findPublishedByTags(paging, Doc.Type.BLOG, tags);
    }

    public PagingList<Doc> findPublishedByDate(Paging paging, LocalDateTime from, LocalDateTime to) {
        return super.findPublishedByDate(paging, Doc.Type.BLOG, from, to);
    }

    public PagingList<Doc> findPublished(Paging paging) {
        return docDAO.findPublished(paging, Doc.Type.BLOG);
    }

    /* We simplify searchTerms - only words and we join with OR operator. */
    public PagingList<Doc> searchPublished(Paging paging, String searchTerms) {
        String[] terms = searchTerms.split("\\s+");
        String fullTextQuery = StringUtils.join(terms, "|");
        try {
            return docDAO.searchPublished(paging, Doc.Type.BLOG, fullTextQuery);
        } catch (RuntimeException e) {
            return new PagingList<>();
        }
    }

    public List<Integer> findAllYears() {
        return blogDAO.findAllYears();
    }

    // == Blog specific methods
    public Doc getPrevBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        return blogDAO.getPrevBlog(currentBlogId, publishedDt);
    }

    public Doc getNextBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        return blogDAO.getNextBlog(currentBlogId, publishedDt);
    }
}
