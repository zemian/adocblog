package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.domain.DocHistory;
import com.zemian.adocblog.data.support.DataUtils;
import com.zemian.adocblog.service.ContentService;
import com.zemian.adocblog.service.PageService;
import com.zemian.adocblog.web.listener.UserSession;
import com.zemian.adocblog.web.listener.UserSessionUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Admin - Page Management UI
 */
@Controller
public class AdminPageController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminPageController.class);

    public static final Paging DEFAULT_PAGING = new Paging(0, 25);

    @Autowired
    private PageService pageService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private Configuration freeMarkerConfig;

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/admin/page/list")
    public ModelAndView list(Paging paging) {
        PagingList<Doc> pages = pageService.findLatest(paging);
        ModelAndView result = new ModelAndView("/admin/page/list");
        result.addObject("pages", pages);
        return result;
    }

    @GetMapping("/admin/page/publish/{pageId}/{contentId}")
    public ModelAndView publish(@PathVariable Integer pageId, @PathVariable Integer contentId,
                                HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);

        Doc page = pageService.get(pageId);

        Content content = new Content();
        content.setContentId(contentId);
        page.setLatestContent(content);
        page.setPublishedUser(userSession.getUser().getUsername());
        page.setPublishedDt(LocalDateTime.now());

        pageService.publish(page);
        LOG.info("Doc {} with contentId {} published by {}",
                pageId, contentId, userSession.getUser().getUsername());

        req.setAttribute("actionSuccessMessage", "Doc " + pageId + " with contentId " + contentId + " has published successfully.");
        return list(DEFAULT_PAGING);
    }

    @GetMapping("/admin/page/unpublish/{pageId}")
    public ModelAndView unpublish(@PathVariable Integer pageId, HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);
        Doc page = pageService.get(pageId);
        if (page.getPublishedContent() == null) {
            throw new AppException("Doc " + pageId + " is not yet published");
        }
        Integer contentId = page.getPublishedContent().getContentId();
        pageService.unpublish(pageId);
        LOG.info("Doc {} with contentId {} unpublished by {}",
                pageId, contentId, userSession.getUser().getUsername());

        req.setAttribute("actionSuccessMessage", "Doc " + pageId + " with contentId " + contentId + " has unpublished successfully.");
        return list(DEFAULT_PAGING);
    }

    @GetMapping("/admin/page/delete/{pageId}")
    public ModelAndView delete(@PathVariable Integer pageId, HttpServletRequest req) {
        String reasonForDelete = null;
        pageService.markForDelete(pageId, reasonForDelete);

        req.setAttribute("actionSuccessMessage", "Doc " + pageId + " has deleted successfully.");
        return list(DEFAULT_PAGING);
    }

    @GetMapping("/admin/page/history/{pageId}")
    public ModelAndView history(@PathVariable Integer pageId) {
        DocHistory pageHistory = pageService.getDocHistory(pageId);
        ModelAndView result = new ModelAndView("/admin/page/history");
        result.addObject("pageHistory", pageHistory);
        return result;
    }

    @GetMapping("/admin/page/create")
    public ModelAndView create() {
        ModelAndView result = new ModelAndView("/admin/page/create");
        return result;
    }

    @PostMapping("/admin/page/create")
    public ModelAndView createPost(HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);

        String title = req.getParameter("title");
        String path = req.getParameter("path");
        String format = req.getParameter("format");
        String contentText = req.getParameter("contentText");

        Doc page = DataUtils.createDoc(Doc.Type.PAGE, Content.Format.valueOf(format),
                userSession.getUser().getUsername(), title, contentText);
        if (StringUtils.isNotEmpty(path)) {
            page.setPath(path);
        }
        pageService.create(page);
        req.setAttribute("actionSuccessMessage", "Doc " + page.getDocId() + " created successfully.");
        return list(DEFAULT_PAGING);
    }

    @GetMapping("/admin/page/edit/{pageId}")
    public ModelAndView edit(@PathVariable Integer pageId) {
        Doc page = pageService.get(pageId);
        String ct = contentService.getContentText(page.getLatestContent().getContentId());
        ModelAndView result = new ModelAndView("/admin/page/edit");
        result.addObject("page", page);
        result.addObject("pageContentText", ct);
        return result;
    }

    @PostMapping("/admin/page/edit")
    public ModelAndView editPost(HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);

        Integer pageId = Integer.parseInt(req.getParameter("pageId"));
        String title = req.getParameter("title");
        String path = req.getParameter("path");
        String format = req.getParameter("format");
        String contentText = req.getParameter("contentText");
        String reasonForEdit = req.getParameter("reasonForEdit");

        Doc page = pageService.get(pageId);
        if (StringUtils.isNotEmpty(path)) {
            page.setPath(path);
        }
        page.getLatestContent().setTitle(title);
        page.getLatestContent().setCreatedUser(userSession.getUser().getUsername());
        page.getLatestContent().setCreatedDt(LocalDateTime.now());
        page.getLatestContent().setFormat(Content.Format.valueOf(format));
        page.getLatestContent().setReasonForEdit(reasonForEdit);
        page.getLatestContent().setContentText(contentText);
        pageService.update(page);

        Integer contentId = page.getLatestContent().getContentId();
        req.setAttribute("actionSuccessMessage", "Doc " + pageId + " with contentId " + contentId + " edited successfully.");
        return list(DEFAULT_PAGING);
    }

    /*
     * A plain Page (FTL) rendering directly to the http response object for rendering. This can
     * be used as a simple "preview" feature when editing `Page` document.
     *
     * We automatically provide dataModel to the template for request, session and servletContext attributes.
     */
    @GetMapping("/admin/page/preview/{pageId}/{contentId}")
    public void preview(@PathVariable Integer pageId, @PathVariable Integer contentId,
                        HttpServletRequest req,
                        HttpServletResponse resp) throws Exception {
        Doc page = pageService.get(pageId);
        String ct = contentService.getContentText(contentId);

        HashMap dataModel = new HashMap<>();

        // Get req attr
        Enumeration<String> names = req.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            dataModel.put(name, req.getAttribute(name));
        }

        // Get session attr
        HttpSession session = req.getSession();
        names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            dataModel.put(name, session.getAttribute(name));
        }

        // Get context attr
        names = servletContext.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            dataModel.put(name, servletContext.getAttribute(name));
        }

        Template template = new Template(page.getPath(), ct, freeMarkerConfig);
        template.process(dataModel, resp.getWriter());
    }
}
