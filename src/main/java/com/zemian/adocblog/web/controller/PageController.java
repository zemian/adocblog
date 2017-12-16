package com.zemian.adocblog.web.controller;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A controller to fetch a published Page and use its "path" to calculate a viewName as response.
 * The SpringMVC setup should retrive the Page content as view for rendering.
 */
@Controller
public class PageController {
    private static Logger LOG = LoggerFactory.getLogger(PageController.class);

    @Autowired
    private PageService pageService;

    /*
    View single page content
     */
    @GetMapping("/page/{pageId}")
    public ModelAndView page(@PathVariable Integer pageId, HttpServletResponse resp) throws IOException {
        try {
            Doc page = pageService.get(pageId);
            String path = page.getPath();
            String viewName = path;
            int idx = path.indexOf(".ftl");
            if (idx > 0) {
                viewName = path.substring(0, idx);
            }
            ModelAndView ret = new ModelAndView(viewName);
            return ret;
        } catch (EmptyResultDataAccessException e) {
            throw new AppException("Page not found.");
        }
    }
}
