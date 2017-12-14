package com.zemian.adocblog.data.domain;

import java.util.ArrayList;
import java.util.List;

public class BlogHistory {
    private Integer blogId;
    private Integer publishedContentId;
    List<Content> contentVers;

    public Integer getPublishedContentId() {
        return publishedContentId;
    }

    public void setPublishedContentId(Integer publishedContentId) {
        this.publishedContentId = publishedContentId;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public List<Content> getContentVers() {
        return contentVers;
    }

    public void setContentVers(List<Content> contentVers) {
        this.contentVers = contentVers;
    }

    @Override
    public String toString() {
        return "BlogHistory{" +
                "blogId=" + blogId +
                "publishedContentId=" + publishedContentId +
                "contentVers.size()=" + (contentVers != null ? contentVers.size() : "null") +
                '}';
    }
}
