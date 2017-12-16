package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.domain.DocHistory;
import com.zemian.adocblog.data.support.DataUtils;
import com.zemian.adocblog.service.BlogService;
import com.zemian.adocblog.service.ContentService;
import com.zemian.adocblog.web.listener.UserSession;
import com.zemian.adocblog.web.listener.UserSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Admin - Blog Management UI
 */
@Controller
public class AdminBlogController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminBlogController.class);

    public static final Paging DEFAULT_PAGING = new Paging(0, 25);

    @Autowired
    private BlogService blogService;

    @Autowired
    private ContentService contentService;

    @GetMapping("/admin/blog/list")
    public ModelAndView list(Paging paging) {
        PagingList<Doc> blogs = blogService.findLatest(paging);
        ModelAndView result = new ModelAndView("/admin/blog/list");
        result.addObject("blogs", blogs);
        return result;
    }

    @GetMapping("/admin/blog/publish/{blogId}/{contentId}")
    public ModelAndView publish(@PathVariable Integer blogId, @PathVariable Integer contentId,
                                HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);

        Doc blog = blogService.get(blogId);

        Content content = new Content();
        content.setContentId(contentId);
        blog.setLatestContent(content);
        blog.setPublishedUser(userSession.getUser().getUsername());
        blog.setPublishedDt(LocalDateTime.now());

        blogService.publish(blog);
        LOG.info("Doc {} with contentId {} published by {}",
                blogId, contentId, userSession.getUser().getUsername());

        req.setAttribute("actionSuccessMessage", "Doc " + blogId + " with contentId " + contentId + " has published successfully.");
        return list(DEFAULT_PAGING);
    }

    @GetMapping("/admin/blog/unpublish/{blogId}")
    public ModelAndView unpublish(@PathVariable Integer blogId, HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);
        Doc blog = blogService.get(blogId);
        if (blog.getPublishedContent() == null) {
            throw new AppException("Doc " + blogId + " is not yet published");
        }
        Integer contentId = blog.getPublishedContent().getContentId();
        blogService.unpublish(blogId);
        LOG.info("Doc {} with contentId {} unpublished by {}",
                blogId, contentId, userSession.getUser().getUsername());

        req.setAttribute("actionSuccessMessage", "Doc " + blogId + " with contentId " + contentId + " has unpublished successfully.");
        return list(DEFAULT_PAGING);
    }

    @GetMapping("/admin/blog/delete/{blogId}")
    public ModelAndView delete(@PathVariable Integer blogId, HttpServletRequest req) {
        String reasonForDelete = null;
        blogService.markForDelete(blogId, reasonForDelete);

        req.setAttribute("actionSuccessMessage", "Doc " + blogId + " has deleted successfully.");
        return list(DEFAULT_PAGING);
    }

    @GetMapping("/admin/blog/history/{blogId}")
    public ModelAndView history(@PathVariable Integer blogId) {
        DocHistory blogHistory = blogService.getDocHistory(blogId);
        ModelAndView result = new ModelAndView("/admin/blog/history");
        result.addObject("blogHistory", blogHistory);
        return result;
    }

    @GetMapping("/admin/blog/create")
    public ModelAndView create() {
        ModelAndView result = new ModelAndView("/admin/blog/create");
        return result;
    }

    @PostMapping("/admin/blog/create")
    public ModelAndView createPost(HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);

        String title = req.getParameter("title");
        String format = req.getParameter("format");
        String contentText = req.getParameter("contentText");

        Doc blog = DataUtils.createDoc(Doc.Type.BLOG, Content.Format.valueOf(format),
                userSession.getUser().getUsername(), title, contentText);
        blogService.create(blog);
        req.setAttribute("actionSuccessMessage", "Doc " + blog.getDocId() + " created successfully.");
        return list(DEFAULT_PAGING);
    }

    @GetMapping("/admin/blog/edit/{blogId}")
    public ModelAndView edit(@PathVariable Integer blogId) {
        Doc blog = blogService.get(blogId);
        String ct = contentService.getContentText(blog.getLatestContent().getContentId());
        ModelAndView result = new ModelAndView("/admin/blog/edit");
        result.addObject("blog", blog);
        result.addObject("blogContentText", ct);
        return result;
    }

    @PostMapping("/admin/blog/edit")
    public ModelAndView editPost(HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);

        Integer blogId = Integer.parseInt(req.getParameter("blogId"));
        String title = req.getParameter("title");
        String format = req.getParameter("format");
        String contentText = req.getParameter("contentText");
        String reasonForEdit = req.getParameter("reasonForEdit");

        Doc blog = blogService.get(blogId);
        blog.getLatestContent().setTitle(title);
        blog.getLatestContent().setCreatedUser(userSession.getUser().getUsername());
        blog.getLatestContent().setCreatedDt(LocalDateTime.now());
        blog.getLatestContent().setFormat(Content.Format.valueOf(format));
        blog.getLatestContent().setReasonForEdit(reasonForEdit);
        blog.getLatestContent().setContentText(contentText);
        blogService.update(blog);

        Integer contentId = blog.getLatestContent().getContentId();
        req.setAttribute("actionSuccessMessage", "Doc " + blogId + " with contentId " + contentId + " edited successfully.");
        return list(DEFAULT_PAGING);
    }

    @GetMapping("/admin/blog/preview/{blogId}/{contentId}")
    public ModelAndView preview(@PathVariable Integer blogId, @PathVariable Integer contentId) {
        // This get will serve as validation.
        Doc blog = blogService.get(blogId);

        Content content = contentService.get(contentId);
        String blogContentText = contentService.getContentHtml(content);

        ModelAndView result = new ModelAndView("/admin/blog/preview");
        result.addObject("blog", blog);
        result.addObject("blogContentText", blogContentText);
        return result;
    }
}
