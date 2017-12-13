package com.zemian.adocblog.service;

import com.zemian.adocblog.data.dao.ContentDAO;
import com.zemian.adocblog.data.domain.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContentService {
    @Autowired
    private ContentDAO contentDAO;

    @Autowired
    private AsciidocService asciidocService;

    public void delete(Integer contentId) {
        contentDAO.delete(contentId);
    }

    public String getContentText(Integer contentId) {
        return contentDAO.getContentText(contentId);
    }

    public String getContentHtml(Content contentMeta) {
        String result;
        String ct = getContentText(contentMeta.getContentId());
        if (contentMeta.getFormat() == Content.Format.ADOC) {
            result = asciidocService.toHtml(ct);
        } else {
            result = ct;
        }
        return result;
    }

    public void create(Content content) {
        contentDAO.create(content);
    }

    public Content get(Integer contentId) {
        return contentDAO.get(contentId);
    }

    public Content getContentMeta(Integer contentId) {
        return contentDAO.getContentMeta(contentId);
    }
}
