package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The home and landing page of the application.
 */
@Controller
public class AdminIndexController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/admin")
    public ModelAndView index() {
        ModelAndView result = new ModelAndView("/admin/index");
        result.addObject("publishedBlogsCount", blogService.getPublishedCount());
        result.addObject("totalBlogsCount", blogService.getTotalCount());
        return result;
    }
}
