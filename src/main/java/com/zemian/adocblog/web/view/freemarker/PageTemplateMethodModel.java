package com.zemian.adocblog.web.view.freemarker;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.service.ContentService;
import com.zemian.adocblog.service.PageService;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PageTemplateMethodModel implements TemplateMethodModelEx {
    @Autowired
    private PageService pageService;

    @Autowired
    private ContentService contentService;

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments.size() < 1) {
            throw new AppException("Missing argument[0] as path");
        }
        String path = ((TemplateScalarModel) arguments.get(0)).getAsString();
        Doc page = pageService.getByPath(path);
        String ct = contentService.getContentHtml(page.getLatestContent());
        return ct;
    }
}
