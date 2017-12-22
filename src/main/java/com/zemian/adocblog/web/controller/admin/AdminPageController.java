package com.zemian.adocblog.web.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Admin - Page Management UI
 */
@Controller
public class AdminPageController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminPageController.class);

    public static final Paging DEFAULT_PAGING = new Paging(0, 25);
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Autowired
    private PageService pageService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private Configuration freeMarkerConfig;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ObjectMapper jsonMapper;

    @GetMapping("/admin/page/list")
    public ModelAndView list(Paging paging) {
        PagingList<Doc> pages = pageService.findLatest(paging);
        ModelAndView result = new ModelAndView("/admin/page/list");
        result.addObject("pages", pages);
        return result;
    }
    
    public ModelAndView list() {
        return list(DEFAULT_PAGING);
    }

    @GetMapping("/admin/page/publish/{pageId}/{contentId}")
    public ModelAndView publish(@PathVariable Integer pageId, @PathVariable Integer contentId,
                                HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);

        Doc page = pageService.get(pageId);
        publishPage(page, contentId, userSession.getUser().getUsername(), LocalDateTime.now());

        req.setAttribute("actionSuccessMessage",
                "Doc " + pageId + " with contentId " + contentId + " has published successfully.");
        return list();
    }

    @GetMapping("/admin/page/publish/{pageId}/{contentId}/{publishDate}")
    public ModelAndView publishByDate(@PathVariable Integer pageId,
                                      @PathVariable Integer contentId,
                                      @PathVariable String publishDate,
                                HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);
        LocalDateTime publishedDt = LocalDateTime.parse(publishDate, YYYY_MM_DD_HH_MM);

        Doc page = pageService.get(pageId);
        publishPage(page, contentId, userSession.getUser().getUsername(), publishedDt);

        req.setAttribute("actionSuccessMessage",
                "Doc " + pageId + " with contentId " + contentId +
                        " has published successfully and publishedDate " + publishDate + ".");
        return list();
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
        return list();
    }

    @GetMapping("/admin/page/delete/{pageId}")
    public ModelAndView delete(@PathVariable Integer pageId, HttpServletRequest req) {
        String reasonForDelete = null;
        pageService.markForDelete(pageId, reasonForDelete);

        req.setAttribute("actionSuccessMessage", "Doc " + pageId + " has deleted successfully.");
        return list();
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
        String btnAction = req.getParameter("btnAction");

        if (StringUtils.isEmpty(title) ||
                StringUtils.isEmpty(path) ||
                StringUtils.isEmpty(contentText)) {
            req.setAttribute("title", title);
            req.setAttribute("contentText", contentText);
            req.setAttribute("format", format);
            req.setAttribute("path", path);

            req.setAttribute("actionErrorMessage", "Invalid inputs");
            return create();
        }

        Doc page = DataUtils.createDoc(Doc.Type.PAGE, Content.Format.valueOf(format),
                userSession.getUser().getUsername(), title, contentText);
        if (StringUtils.isNotEmpty(path)) {
            page.setPath(path);
        }
        pageService.create(page);

        String message = "Doc " + page.getDocId() + " created successfully.";

        if ("publish".equals(btnAction)) {
            publishPage(page, page.getLatestContent().getContentId(), userSession.getUser().getUsername(), LocalDateTime.now());
            message += " And the content has published.";
        }

        req.setAttribute("actionSuccessMessage", message);

        return list();
    }

    private void publishPage(Doc page, Integer contentId, String username, LocalDateTime publishedDt) {
        Content content = new Content();
        content.setContentId(contentId);
        page.setLatestContent(content);
        page.setPublishedUser(username);
        page.setPublishedDt(publishedDt);

        pageService.publish(page);
        LOG.info("Doc {} with contentId {} published by {}",
                page.getDocId(), contentId, username);

    }

    @GetMapping("/admin/page/edit/{pageId}")
    public ModelAndView edit(@PathVariable Integer pageId) {
        Doc page = pageService.get(pageId);
        String ct = contentService.getContentText(page.getLatestContent().getContentId());
        page.getLatestContent().setContentText(ct);
        return edit(page);
    }

    private ModelAndView edit(Doc page) {
        ModelAndView result = new ModelAndView("/admin/page/edit");
        result.addObject("page", page);
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
        String btnAction = req.getParameter("btnAction");

        Doc page = pageService.get(pageId);
        page.getLatestContent().setTitle(title);
        page.getLatestContent().setCreatedUser(userSession.getUser().getUsername());
        page.getLatestContent().setCreatedDt(LocalDateTime.now());
        page.getLatestContent().setFormat(Content.Format.valueOf(format));
        page.getLatestContent().setReasonForEdit(reasonForEdit);
        page.getLatestContent().setContentText(contentText);

        if (StringUtils.isEmpty(title) ||
                StringUtils.isEmpty(path) ||
                StringUtils.isEmpty(contentText)) {
            req.setAttribute("page", page);
            req.setAttribute("actionErrorMessage", "Invalid inputs");
            return edit(page);
        }


        if (StringUtils.isNotEmpty(path)) {
            page.setPath(path);
        }
        pageService.update(page);

        Integer contentId = page.getLatestContent().getContentId();

        String message = "Doc " + pageId + " with contentId " + contentId + " edited successfully.";

        if ("publish".equals(btnAction)) {
            publishPage(page, contentId, userSession.getUser().getUsername(), LocalDateTime.now());
            message += " And the content has published.";
        }

        req.setAttribute("actionSuccessMessage", message);

        return list();
    }

    /*
     * A plain Page (FTL) rendering directly to the http response object for rendering. This can
     * be used as a simple "preview" feature when editing `Page` document.
     *
     * We automatically provide dataModel to the template for request, session and servletContext attributes.
     * If you need more data to preview the template Page, you may use "dataPath" parameter to specify
     * a test data Page (JSON) with the path equals to same name.
     */
    @GetMapping("/admin/page/preview/{pageId}/{contentId}")
    public void preview(@PathVariable Integer pageId,
                        @PathVariable Integer contentId,
                        @RequestParam(value = "dataPath", defaultValue = "") String dataPath,
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

        // Check for addition data from dataPath param
        if (StringUtils.isNotEmpty(dataPath)) {
            Doc dataDoc = pageService.getByPath(dataPath);
            if (dataDoc.getLatestContent().getFormat() == Content.Format.JSON) {
                String dataCt = contentService.getContentText(dataDoc.getLatestContent().getContentId());
                Map<String, Object> jsonData = jsonMapper.readValue(dataCt, Map.class);
                dataModel.putAll(jsonData);
            }
        }

        Template template = new Template(page.getPath(), ct, freeMarkerConfig);
        template.process(dataModel, resp.getWriter());
    }
}
