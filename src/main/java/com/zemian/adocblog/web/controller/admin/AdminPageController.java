package com.zemian.adocblog.web.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.support.DataUtils;
import com.zemian.adocblog.service.ContentService;
import com.zemian.adocblog.service.PageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Admin - Page Management UI
 */
@Controller
public class AdminPageController extends AbstractDocController {
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
    public ModelAndView list(Paging paging, HttpServletRequest req) {
        if (StringUtils.isEmpty(req.getParameter("size"))) {
            paging.setSize(defaultPagingSize);
        }
        return list("/admin/page/list", Doc.Type.PAGE, paging);
    }

    @GetMapping("/admin/page/publish/{pageId}/{contentId}")
    public ModelAndView publish(@PathVariable Integer pageId, @PathVariable Integer contentId) {
        Doc doc = docService.get(pageId);
        LocalDateTime publishDate = LocalDateTime.now();
        return getView("/admin/page/publish",
                "doc", doc, "contentId", contentId, "publishDate", publishDate);
    }

    @PostMapping("/admin/page/publish")
    public ModelAndView publishPost(HttpServletRequest req, RedirectAttributes redirectAttrs) {
        Integer docId = Integer.parseInt(req.getParameter("docId"));
        Integer contentId = Integer.parseInt(req.getParameter("contentId"));
        String publishDate = req.getParameter("publishDate");
        return handlePublishByDate("/admin/page/list", docId, contentId, publishDate, req, redirectAttrs);
    }

    @GetMapping("/admin/page/unpublish/{pageId}")
    public ModelAndView unpublish(@PathVariable Integer pageId, HttpServletRequest req, RedirectAttributes redirectAttrs) {
        return handleUnpublish("/admin/page/list", pageId, req, redirectAttrs);
    }

    @GetMapping("/admin/page/detail/{pageId}")
    public ModelAndView detail(@PathVariable Integer pageId) {
        Doc doc = docService.get(pageId);
        return getView("/admin/page/detail", "doc", doc);
    }

    @GetMapping("/admin/page/delete/{pageId}")
    public ModelAndView delete(@PathVariable Integer pageId) {
        Doc doc = docService.get(pageId);
        return getView("/admin/page/delete", "doc", doc);
    }

    @PostMapping("/admin/page/delete")
    public ModelAndView deletePost(HttpServletRequest req, RedirectAttributes redirectAttrs) {
        Integer docId = Integer.parseInt(req.getParameter("docId"));
        return handleDelete("/admin/page/list", docId, req, redirectAttrs);
    }

    @GetMapping("/admin/page/history/{pageId}")
    public ModelAndView history(@PathVariable Integer pageId) {
        return handleHistory("/admin/page/history", pageId);
    }

    @Controller
    public class CreateForm {
        @GetMapping("/admin/page/create")
        public ModelAndView create() {
            return getView("/admin/page/create", "doc", DataUtils.createEmptyDoc(Doc.Type.PAGE));
        }

        @PostMapping("/admin/page/create")
        public ModelAndView createSubmit(
                HttpServletRequest req,
                RedirectAttributes redirectAttrs,
                @ModelAttribute Doc doc,
                BindingResult bindingResult) {
            if (!validDoc(doc, bindingResult)) {
                return getErrorView("/admin/page/create", bindingResult, "doc", doc);
            }
            return handleCreateSubmit("/admin/page/list", doc, req, redirectAttrs);
        }
    }

    @Controller
    public class EditForm {
        @GetMapping("/admin/page/edit/{blogId}")
        public ModelAndView edit(@PathVariable Integer blogId) {
            return handleEditView("/admin/page/edit", blogId);
        }

        @PostMapping("/admin/page/edit")
        public ModelAndView editSubmit(
                HttpServletRequest req,
                RedirectAttributes redirectAttrs,
                @ModelAttribute Doc doc,
                BindingResult bindingResult) {
            if (!validDoc(doc, bindingResult)) {
                return getErrorView("/admin/page/create", bindingResult, "doc", doc);
            }
            return handlEditSubmit("/admin/page/list", doc, req, redirectAttrs);
        }
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
