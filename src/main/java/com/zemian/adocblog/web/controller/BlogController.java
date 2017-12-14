package com.zemian.adocblog.web.controller;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Blog;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.service.BlogService;
import com.zemian.adocblog.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Public User Viewing of the Blog App. It will return view based on a themeName.
 */
@Controller
public class BlogController {
    private static Logger LOG = LoggerFactory.getLogger(BlogController.class);

    @Value("${app.web.themeName}")
    private String themeName;

    @Value("${app.web.numOfRecentPosts}")
    private int numOfRecentPosts = 5;

    @Autowired
    private ContentService contentService;

    @Autowired
    private BlogService blogService;

    private String getThemeViewName(String name) {
        return "/themes/" + themeName + name;
    }

    /*
    Landing home page - preview of recent blogs
     */
    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView result = new ModelAndView(getThemeViewName("/index"));
        PagingList<Blog> blogs = blogService.findPublished(new Paging(0, numOfRecentPosts + 1));
        if (blogs.getList().size() > 0) {
            // Fetch the first blog content text, and remove from list
            Blog blog = blogs.getList().remove(0);
            String ct = contentService.getContentHtml(blog.getPublishedContent());
            blog.getPublishedContent().setContentText(ct);
            result.addObject("blog", blog);
        }

        // Recent blogs
        result.addObject("blogs", blogs);
        return result;
    }

    @GetMapping("/about")
    public ModelAndView about() {
        ModelAndView result = new ModelAndView(getThemeViewName("/about"));
        return result;
    }

    /*
    List of published blogs
     */
    @GetMapping("/archive")
    public ModelAndView blogs(Paging paging) {
        PagingList<Blog> blogs = blogService.findPublished(paging);
        ModelAndView result = new ModelAndView(getThemeViewName("/archive"));
        result.addObject("blogs", blogs);
        return result;
    }

    /*
    View single blog content
     */
    @GetMapping("/blog/{blogId}")
    public ModelAndView blog(@PathVariable Integer blogId) {
        Blog blog = blogService.get(blogId);
        Content publishedContent = blog.getPublishedContent();
        if (publishedContent == null) {
            throw new AppException("Blog is not published yet.");
        }
        String ct = contentService.getContentHtml(publishedContent);
        blog.getPublishedContent().setContentText(ct);

        // Fetch previous and next blog data if there is any
        Blog prevBlog = blogService.findPrevBlog(blog.getBlogId(), blog.getPublishedDt());
        Blog nextBlog = blogService.findNextBlog(blog.getBlogId(), blog.getPublishedDt());

        ModelAndView result = new ModelAndView(getThemeViewName("/blog"));
        result.addObject("blog", blog);
        result.addObject("prevBlog", prevBlog);
        result.addObject("nextBlog", nextBlog);
        return result;
    }

    /*
    Search of published blogs using POSTGRES search ts_query terms
     */
    @RequestMapping(value = "/search", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView blogs(Paging paging, HttpServletRequest req) {
        String searchTerms = req.getParameter("searchTerms");
        PagingList<Blog> blogs = blogService.searchPublished(paging, searchTerms);
        ModelAndView result = new ModelAndView(getThemeViewName("/search"));
        result.addObject("blogs", blogs);
        result.addObject("searchTerms", searchTerms);
        return result;
    }
}
