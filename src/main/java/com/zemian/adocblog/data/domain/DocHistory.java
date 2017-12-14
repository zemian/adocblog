package com.zemian.adocblog.data.domain;

import java.util.List;

public class DocHistory {
    private Integer docId;
    private Integer publishedContentId;
    List<Content> contentVers;

    public Integer getPublishedContentId() {
        return publishedContentId;
    }

    public void setPublishedContentId(Integer publishedContentId) {
        this.publishedContentId = publishedContentId;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public List<Content> getContentVers() {
        return contentVers;
    }

    public void setContentVers(List<Content> contentVers) {
        this.contentVers = contentVers;
    }

    @Override
    public String toString() {
        return "PageHistory{" +
                "pageId=" + docId +
                "publishedContentId=" + publishedContentId +
                "contentVers.size()=" + (contentVers != null ? contentVers.size() : "null") +
                '}';
    }
}
