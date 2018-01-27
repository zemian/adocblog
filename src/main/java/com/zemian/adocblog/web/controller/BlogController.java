package com.zemian.adocblog.web.controller;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.service.BlogService;
import com.zemian.adocblog.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Public User Viewing of the Blog App. It will return a view based on a themeName.
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

    /* Landing home page - preview of recent blogs */
    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView result = new ModelAndView(getThemeViewName("/index"));
        PagingList<Doc> blogs = blogService.findPublished(new Paging(0, numOfRecentPosts + 1));
        if (blogs.getList().size() > 0) {
            // Fetch the first blog content text, and remove from list
            Doc blog = blogs.getList().remove(0);
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

    private ModelAndView archiveView(PagingList<Doc> blogs) {
        List<String> tags = blogService.findAllExpandedTags();
        List<Integer> years = blogService.findAllYears();
        ModelAndView result = new ModelAndView(getThemeViewName("/archive"));
        result.addObject("blogs", blogs);
        result.addObject("tags", tags);
        result.addObject("years", years);
        return result;
    }

    /* List of published blogs */
    @GetMapping("/archive")
    public ModelAndView archive(Paging paging) {
        PagingList<Doc> blogs = blogService.findPublished(paging);
        return archiveView(blogs);
    }

    /* List of published blogs by tags */
    @GetMapping("/archive/tags/{name}")
    public ModelAndView archiveByTag(Paging paging, @PathVariable String name) {
        PagingList<Doc> blogs = blogService.findPublishedByTags(paging, name);
        return archiveView(blogs);
    }

    /* List of published blogs by dates (year) */
    @GetMapping("/archive/{year}")
    public ModelAndView archiveByYear(Paging paging, @PathVariable String year) {
        LocalDateTime from = LocalDateTime.parse(year + "-01-01T00:00");
        LocalDateTime to = from.plusYears(1L).minusSeconds(1); // Use -1 sec to ensure not to include next year.
        PagingList<Doc> blogs = blogService.findPublishedByDate(paging, from, to);
        return archiveView(blogs);
    }

    /* List of published blogs by dates (year/month) */
    @GetMapping("/archive/{year}/{month}")
    public ModelAndView archiveByYearMonth(Paging paging, @PathVariable String year, @PathVariable String month) {
        if (month.length() == 1) {
            month = "0" + month;
        }
        LocalDateTime from = LocalDateTime.parse(year + "-" + month + "-01T00:00");
        LocalDateTime to = from.plusYears(1L).minusSeconds(1); // Use -1 sec to ensure not to include next year.
        PagingList<Doc> blogs = blogService.findPublishedByDate(paging, from, to);
        ModelAndView result = new ModelAndView(getThemeViewName("/archive"));
        result.addObject("blogs", blogs);
        return result;
    }

    /* View single blog content */
    @GetMapping("/blog/{blogId}")
    public ModelAndView blog(@PathVariable Integer blogId) {
        ModelAndView result = new ModelAndView(getThemeViewName("/blog"));
        try {
            Doc blog = blogService.get(blogId);
            Content publishedContent = blog.getPublishedContent();
            if (publishedContent == null) {
                throw new AppException("Blog is not published yet.");
            }
            String ct = contentService.getContentHtml(publishedContent);
            blog.getPublishedContent().setContentText(ct);

            // Fetch previous and next blog data if there is any
            Doc prevBlog = blogService.getPrevBlog(blog.getDocId(), blog.getPublishedDt());
            Doc nextBlog = blogService.getNextBlog(blog.getDocId(), blog.getPublishedDt());

            result.addObject("blog", blog);
            result.addObject("prevBlog", prevBlog);
            result.addObject("nextBlog", nextBlog);
            return result;
        } catch (EmptyResultDataAccessException e) {
            result.addObject("errorMessage", "Blog not found.");
            return result;
        } catch (RuntimeException e) {
            result.addObject("errorMessage", e.getMessage());
            return result;
        }
    }

    /* Search of published blogs using POSTGRES search ts_query terms */
    @RequestMapping(value = "/search", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView search(Paging paging, HttpServletRequest req) {
        String searchTerms = req.getParameter("searchTerms");
        PagingList<Doc> blogs = blogService.searchPublished(paging, searchTerms);
        ModelAndView result = new ModelAndView(getThemeViewName("/search"));
        result.addObject("blogs", blogs);
        result.addObject("searchTerms", searchTerms);
        return result;
    }
}
