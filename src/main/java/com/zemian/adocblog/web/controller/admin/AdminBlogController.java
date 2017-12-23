package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.domain.Doc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Admin - Blog Management UI
 */
@Controller
public class AdminBlogController extends AbstractDocController {
    @GetMapping("/admin/blog/list")
    public ModelAndView list(Paging paging) {
        return list("/admin/blog/list", Doc.Type.BLOG, paging);
    }

    @GetMapping("/admin/blog/publish/{blogId}/{contentId}")
    public ModelAndView publish(@PathVariable Integer blogId, @PathVariable Integer contentId, HttpServletRequest req) {

        return publish("/admin/blog/list", Doc.Type.BLOG, blogId, contentId, req);
    }

    @GetMapping("/admin/blog/publish/{blogId}/{contentId}/{publishDate}")
    public ModelAndView publishByDate(@PathVariable Integer blogId,
                                      @PathVariable Integer contentId,
                                      @PathVariable String publishDate,
                                HttpServletRequest req) {
        return publishByDate("/admin/blog/list", Doc.Type.BLOG, blogId, contentId, publishDate, req);
    }

    @GetMapping("/admin/blog/unpublish/{blogId}")
    public ModelAndView unpublish(@PathVariable Integer blogId, HttpServletRequest req) {
        return unpublish("/admin/blog/list", Doc.Type.BLOG, blogId, req);
    }

    @GetMapping("/admin/blog/delete/{blogId}")
    public ModelAndView delete(@PathVariable Integer blogId, HttpServletRequest req) {
        return delete("/admin/blog/list", Doc.Type.BLOG, blogId, req);
    }

    @GetMapping("/admin/blog/history/{blogId}")
    public ModelAndView history(@PathVariable Integer blogId) {
        return history("/admin/blog/history", blogId);
    }

    @GetMapping("/admin/blog/create")
    public ModelAndView create() {
        ModelAndView result = new ModelAndView("/admin/blog/create");
        return result;
    }

    @PostMapping("/admin/blog/create")
    public ModelAndView createPost(HttpServletRequest req) {
        return createPost("/admin/blog/list", Doc.Type.BLOG, req);
    }

    @GetMapping("/admin/blog/edit/{blogId}")
    public ModelAndView edit(@PathVariable Integer blogId) {
        return edit("/admin/blog/edit", blogId);
    }

    @PostMapping("/admin/blog/edit")
    public ModelAndView editPost(HttpServletRequest req) {
        return editPost("/admin/blog/list", Doc.Type.BLOG, req);
    }

    @GetMapping("/admin/blog/preview/{blogId}/{contentId}")
    public ModelAndView preview(@PathVariable Integer blogId, @PathVariable Integer contentId) {
        return preview("/admin/blog/list", blogId, contentId);
    }
}
