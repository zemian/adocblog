package com.zemian.adocblog.service;

import com.zemian.adocblog.data.dao.BlogDAO;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Blog;
import com.zemian.adocblog.data.domain.BlogHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * A Blog is a Doc with type = BLOG.
 */
@Service
@Transactional
public class BlogService {

    @Autowired
    private BlogDAO blogDAO;

    public int getPublishedCount() {
        return blogDAO.getPublishedCount();
    }

    public int getTotalCount() {
        return blogDAO.getTotalCount();
    }

    public void create(Blog blog) {
        blogDAO.create(blog);
    }

    public Blog get(Integer id) {
        return blogDAO.get(id);
    }

    public BlogHistory getBlogHistory(Integer blogId) {
        return blogDAO.getBlogHistory(blogId);
    }

    public void publish(Blog blog) {
        blogDAO.publish(blog);
    }

    public void unpublish(Integer blogId) {
        blogDAO.unpublish(blogId);
    }

    public void update(Blog blog) {
        blog.getLatestContent().setVersion(blog.getLatestContent().getVersion() + 1);
        blogDAO.update(blog);
    }

    public void markForDelete(Integer blogId, String reasonForDelete) {
        blogDAO.markForDelete(blogId, reasonForDelete);
    }

    public void delete(Integer blogId) {
        blogDAO.delete(blogId);
    }

    public PagingList<Blog> findLatest(Paging paging) {
        return blogDAO.findLatest(paging);
    }

    public PagingList<Blog> findPublished(Paging paging) {
        return blogDAO.findPublished(paging);
    }

    public PagingList<Blog> searchPublished(Paging paging, String searchTerms) {
        return blogDAO.searchPublished(paging, searchTerms);
    }

    public Blog findPrevBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        return blogDAO.findPrevBlog(currentBlogId, publishedDt);
    }

    public Blog findNextBlog(Integer currentBlogId, LocalDateTime publishedDt) {
        return blogDAO.findNextBlog(currentBlogId, publishedDt);
    }
}
