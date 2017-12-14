//package com.zemian.adocblog.web.controller.admin;
//
//import com.zemian.adocblog.data.dao.Paging;
//import com.zemian.adocblog.data.dao.PagingList;
//import com.zemian.adocblog.data.domain.Content;
//import com.zemian.adocblog.data.domain.Blog;
//import com.zemian.adocblog.service.AsciidocService;
//import com.zemian.adocblog.service.ContentService;
//import com.zemian.adocblog.service.PageService;
//import com.zemian.adocblog.web.listener.UserSession;
//import com.zemian.adocblog.web.listener.UserSessionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import java.time.LocalDateTime;
//
///**
// * Admin - Doc Management UI
// */
//@Controller
//public class AdminPageController {
//    private static final Logger LOG = LoggerFactory.getLogger(AdminPageController.class);
//
//    public static final Paging DEFAULT_PAGING = new Paging(0, 25);
//
//    @Autowired
//    private PageService pageService;
//
//    @Autowired
//    private ContentService contentService;
//
//    @Autowired
//    private AsciidocService asciidocService;
//
//    @GetMapping("/admin/page/list")
//    public ModelAndView list(Paging paging) {
//        PagingList<Blog> pages = pageService.findLatest(paging);
//        ModelAndView result = new ModelAndView("/admin/page/list");
//        result.addObject("pages", pages);
//        return result;
//    }
//
//    @GetMapping("/admin/page/markForDelete/{pageId}")
//    public ModelAndView markForDelete(@PathVariable Integer pageId, HttpServletRequest req) {
//        pageService.markForDelete(pageId);
//
//        req.setAttribute("actionSuccessMessage", "Doc " + pageId + " has deleted successfully.");
//        return list(DEFAULT_PAGING);
//    }
//
//    @GetMapping("/admin/page/create")
//    public ModelAndView create() {
//        ModelAndView result = new ModelAndView("/admin/page/create");
//        return result;
//    }
//
//    @PostMapping("/admin/page/create")
//    public ModelAndView createPost(HttpServletRequest req) {
//        UserSession userSession = UserSessionUtils.getUserSession(req);
//
//        String pathName = req.getParameter("pathName");
//        String format = req.getParameter("format");
//        String contentText = req.getParameter("contentText");
//
//        Content content = new Content();
//        content.setCreatedUser(userSession.getUser().getUsername());
//        content.setCreatedDt(LocalDateTime.now());
//        content.setFormat(Content.Format.valueOf(format));
//        content.setContentText(contentText);
//
//        Blog page = new Blog();
//        page.setPathName(pathName);
//        page.setContent(content);
//
//        pageService.create(page);
//        Integer pageId = page.getDocId();
//        req.setAttribute("actionSuccessMessage", "Doc " + pageId + " created successfully.");
//        return list(DEFAULT_PAGING);
//    }
//
//    @GetMapping("/admin/page/edit/{pageId}")
//    public ModelAndView edit(@PathVariable Integer pageId) {
//        Blog page = pageService.get(pageId);
//        String ct = contentService.getContentText(page.getContent().getContentId());
//        ModelAndView result = new ModelAndView("/admin/page/edit");
//        result.addObject("page", page);
//        result.addObject("pageContentText", ct);
//        return result;
//    }
//
//    @PostMapping("/admin/page/edit")
//    public ModelAndView editPost(HttpServletRequest req) {
//        UserSession userSession = UserSessionUtils.getUserSession(req);
//
//        Integer pageId = Integer.parseInt(req.getParameter("pageId"));
//        String pathName = req.getParameter("pathName");
//        String format = req.getParameter("format");
//        String contentText = req.getParameter("contentText");
//        String contentVersionReason = req.getParameter("reason");
//
//        Blog page = pageService.get(pageId);
//        //page.setPath(pathName);
//        page.getContent().setCreatedUser(userSession.getUser().getUsername());
//        page.getContent().setCreatedDt(LocalDateTime.now());
//        page.getContent().setFormat(Content.Format.valueOf(format));
//        page.getContent().setReasonForEdit(contentVersionReason);
//        page.getContent().setContentText(contentText);
//
//        pageService.update(page);
//
//        Integer contentId = page.getContent().getContentId();
//        req.setAttribute("actionSuccessMessage", "Doc " + pageId + " with contentId " + contentId + " edited successfully.");
//        return list(DEFAULT_PAGING);
//    }
//
//    @GetMapping("/admin/page/preview/{pageId}/{contentId}")
//    public ModelAndView preview(@PathVariable Integer pageId, @PathVariable Integer contentId) {
//        Blog page = pageService.get(pageId);
//        String ct = contentService.getContentText(contentId);
//        String pageContentText = asciidocService.toHtml(ct);
//
//        ModelAndView result = new ModelAndView("/admin/page/preview");
//        result.addObject("page", page);
//        result.addObject("pageContentText", pageContentText);
//        return result;
//    }
//}
