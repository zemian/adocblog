package com.zemian.adocblog.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The home and landing page of the application.
 *
 * We will delegate index to BlogController.index().
 */
@Controller
public class IndexController {
    @Autowired
    private BlogController blogController;

    // We mapped "/index" inside BlogController.index().
    @GetMapping("/")
    public ModelAndView index() {
        return blogController.index();
    }
}
