package com.zemian.adocblog.web.view.freemarker;

import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.service.ContentService;
import com.zemian.adocblog.service.PageService;
import freemarker.cache.TemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PageTemplateLoader implements TemplateLoader {
    private static final Logger LOG = LoggerFactory.getLogger(PageTemplateLoader.class);

    @Autowired
    private PageService pageService;

    @Autowired
    private ContentService contentService;

    @Override
    public Object findTemplateSource(String name) throws IOException {
        Doc page = null;
        try {
            page = pageService.getByPath(name);
        } catch (Exception e) {
            // It's okay to not find the page!
            LOG.trace("Page not found from DB: " + name);
        }
        return page;
    }

    @Override
    public long getLastModified(Object templateSource) {
        Doc doc = (Doc) templateSource;
        LocalDateTime dt = doc.getPublishedDt();
        long ret = dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return ret;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        Doc doc = (Doc) templateSource;
        String ct = contentService.getContentText(doc.getPublishedContent().getContentId());
        StringReader reader = new StringReader(ct);
        return reader;
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
        // Do nothing.
    }
}
