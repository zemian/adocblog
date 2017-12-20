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
import org.apache.commons.lang3.StringUtils;
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

    private ModelAndView list()  {
        return list(DEFAULT_PAGING);
    }

    @GetMapping("/admin/blog/publish/{blogId}/{contentId}")
    public ModelAndView publish(@PathVariable Integer blogId, @PathVariable Integer contentId,
                                HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);

        Doc blog = blogService.get(blogId);
        publishBlog(blog, contentId, userSession.getUser().getUsername());

        req.setAttribute("actionSuccessMessage", "Doc " + blogId + " with contentId " + contentId + " has published successfully.");
        return list();
    }

    private void publishBlog(Doc blog, Integer contentId, String username) {
        Content content = new Content();
        content.setContentId(contentId);
        blog.setLatestContent(content);
        blog.setPublishedUser(username);
        blog.setPublishedDt(LocalDateTime.now());

        blogService.publish(blog);
        LOG.info("Doc {} with contentId {} published by {}",
                blog.getDocId(), contentId, username);

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
        return list();
    }

    @GetMapping("/admin/blog/delete/{blogId}")
    public ModelAndView delete(@PathVariable Integer blogId, HttpServletRequest req) {
        String reasonForDelete = null;
        blogService.markForDelete(blogId, reasonForDelete);

        req.setAttribute("actionSuccessMessage", "Doc " + blogId + " has deleted successfully.");
        return list();
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
        String btnAction = req.getParameter("btnAction");

        if (StringUtils.isEmpty(title) ||
                StringUtils.isEmpty(contentText)) {
            req.setAttribute("title", title);
            req.setAttribute("contentText", contentText);
            req.setAttribute("format", format);

            req.setAttribute("actionErrorMessage", "Invalid inputs");
            return create();
        }

        Doc blog = DataUtils.createDoc(Doc.Type.BLOG, Content.Format.valueOf(format),
                userSession.getUser().getUsername(), title, contentText);
        blogService.create(blog);
        String message = "Doc " + blog.getDocId() + " created successfully.";

        if ("publish".equals(btnAction)) {
            publishBlog(blog, blog.getLatestContent().getContentId(), userSession.getUser().getUsername());
            message += " And the content has published.";
        }

        req.setAttribute("actionSuccessMessage", message);
        return list();
    }

    @GetMapping("/admin/blog/edit/{blogId}")
    public ModelAndView edit(@PathVariable Integer blogId) {
        Doc blog = blogService.get(blogId);
        String ct = contentService.getContentText(blog.getLatestContent().getContentId());
        blog.getLatestContent().setContentText(ct);
        return edit(blog);
    }

    private ModelAndView edit(Doc blog) {
        ModelAndView result = new ModelAndView("/admin/blog/edit");
        result.addObject("blog", blog);
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
        String btnAction = req.getParameter("btnAction");

        Doc blog = blogService.get(blogId);
        blog.getLatestContent().setTitle(title);
        blog.getLatestContent().setCreatedUser(userSession.getUser().getUsername());
        blog.getLatestContent().setCreatedDt(LocalDateTime.now());
        blog.getLatestContent().setFormat(Content.Format.valueOf(format));
        blog.getLatestContent().setReasonForEdit(reasonForEdit);
        blog.getLatestContent().setContentText(contentText);

        if (StringUtils.isEmpty(title) ||
                StringUtils.isEmpty(contentText)) {
            req.setAttribute("actionErrorMessage", "Invalid inputs");
            req.setAttribute("blog", blog);
            return edit(blog);
        }

        blogService.update(blog);

        Integer contentId = blog.getLatestContent().getContentId();
        String message = "Doc " + blogId + " with contentId " + contentId + " edited successfully.";

        if ("publish".equals(btnAction)) {
            publishBlog(blog, contentId, userSession.getUser().getUsername());
            message += " And the content has published.";
        }

        req.setAttribute("actionSuccessMessage", message);

        return list();
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
