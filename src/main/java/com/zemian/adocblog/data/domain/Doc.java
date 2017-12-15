package com.zemian.adocblog.data.domain;

import java.time.LocalDateTime;

public class Doc {

    public static enum Type {
        PAGE, BLOG;
    }

    private Integer docId;
    private String path;
    private Type type;
    private Content latestContent;
    private Content publishedContent;
    private String publishedUser;
    private LocalDateTime publishedDt;

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    @Override
    public String toString() {
        String pathIdIfDiffFromId = "";
        if (("" + docId).equals(getPath())) {
            pathIdIfDiffFromId = ", path=" + getPath();
        }
        return "Doc{" +
                "docId=" + docId +
                ", type=" + type +
                pathIdIfDiffFromId +
                ", latestContent.contentId=" + (latestContent != null ? latestContent.getContentId() : "null") +
                ", publishedContent.contentId=" + (publishedContent != null ? publishedContent.getContentId() : "null") +
                '}';
    }
}
