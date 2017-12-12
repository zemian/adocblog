package com.zemian.adocblog.data.domain;

import java.time.LocalDateTime;

public class Blog {
    private Integer blogId;
    private Content latestContent;
    private Content publishedContent;
    private String publishedUser;
    private LocalDateTime publishedDt;

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public Content getLatestContent() {
        return latestContent;
    }

    public void setLatestContent(Content latestContent) {
        this.latestContent = latestContent;
    }

    public Content getPublishedContent() {
        return publishedContent;
    }

    public void setPublishedContent(Content publishedContent) {
        this.publishedContent = publishedContent;
    }

    public String getPublishedUser() {
        return publishedUser;
    }

    public void setPublishedUser(String publishedUser) {
        this.publishedUser = publishedUser;
    }

    public LocalDateTime getPublishedDt() {
        return publishedDt;
    }

    public void setPublishedDt(LocalDateTime publishedDt) {
        this.publishedDt = publishedDt;
    }
}
