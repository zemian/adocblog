package com.zemian.adocblog.web.view.freemarker;

import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.service.PageService;
import freemarker.cache.TemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Reader;

public class PageTemplateLoader implements TemplateLoader {
    private static final Logger LOG = LoggerFactory.getLogger(PageTemplateLoader.class);

    @Autowired
    private PageService pageService;

    @Override
    public Object findTemplateSource(String name) throws IOException {
        Doc page = null;
        try {
            page = pageService.getByPath(name);
        } catch (Exception e) {
            // It's okay to not find the page!
            LOG.debug("Page not found for FTL loader: " + name);
        }
        return page;
    }

    @Override
    public long getLastModified(Object templateSource) {
        return 0;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        return null;
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {

    }
}
