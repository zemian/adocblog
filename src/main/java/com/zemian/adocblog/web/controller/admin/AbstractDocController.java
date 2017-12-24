package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.domain.DocHistory;
import com.zemian.adocblog.data.support.DataUtils;
import com.zemian.adocblog.service.ContentService;
import com.zemian.adocblog.service.DocService;
import com.zemian.adocblog.web.listener.UserSession;
import com.zemian.adocblog.web.listener.UserSessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base class to support Doc related processing.
 */
public abstract class AbstractDocController {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDocController.class);
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Value("${app.web.defaultPagingSize}")
    protected int defaultPagingSize;

    @Autowired
    protected DocService docService;

    @Autowired
    protected ContentService contentService;

    protected ModelAndView list(String viewName, Doc.Type type, Paging paging) {
        PagingList<Doc> docs = docService.findLatest(paging, type);
        ModelAndView result = new ModelAndView(viewName);
        result.addObject("docs", docs);
        return result;
    }

    private ModelAndView list(String viewName, Doc.Type type)  {
        return list(viewName, type, new Paging(0, defaultPagingSize));
    }

    protected ModelAndView publish(String viewName, Doc.Type type,
                                   Integer docId, Integer contentId, HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);
        Doc page = docService.get(docId);
        publishDoc(page, contentId, userSession.getUser().getUsername(), LocalDateTime.now());

        req.setAttribute("actionSuccessMessage",
                "Doc " + docId + " with contentId " + contentId + " has published successfully.");
        return list(viewName, type);
    }

    private void publishDoc(Doc doc, Integer contentId, String username, LocalDateTime publishedDt) {
        Content content = new Content();
        content.setContentId(contentId);
        doc.setLatestContent(content);
        doc.setPublishedUser(username);
        doc.setPublishedDt(publishedDt);

        docService.publish(doc);
        LOG.info("Doc {} with contentId {} is published by {}", doc.getDocId(), contentId, username);
    }

    protected ModelAndView publishByDate(String viewName, Doc.Type type,
                                         Integer docId, Integer contentId, String publishDate, HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);
        LocalDateTime publishedDt = LocalDateTime.parse(publishDate, YYYY_MM_DD_HH_MM);

        Doc doc = docService.get(docId);
        publishDoc(doc, contentId, userSession.getUser().getUsername(), publishedDt);

        req.setAttribute("actionSuccessMessage",
                "Doc " + docId + " with contentId " + contentId +
                        " has published successfully and publishedDate " + publishDate + ".");
        return list(viewName, type);
    }

    protected ModelAndView unpublish(String viewName, Doc.Type type,
                                     Integer docId, HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);
        Doc doc = docService.get(docId);
        if (doc.getPublishedContent() == null) {
            throw new AppException("Doc " + docId + " is not yet published");
        }
        Integer contentId = doc.getPublishedContent().getContentId();
        docService.unpublish(docId);
        LOG.info("Doc {} with contentId {} unpublished by {}",
                docId, contentId, userSession.getUser().getUsername());

        req.setAttribute("actionSuccessMessage",
                "Doc " + docId + " with contentId " + contentId + " has unpublished successfully.");
        return list(viewName, type);
    }

    protected ModelAndView delete(String viewName, Doc.Type type, Integer docId, HttpServletRequest req) {
        String reasonForDelete = null;
        docService.markForDelete(docId, reasonForDelete);

        req.setAttribute("actionSuccessMessage", "Doc " + docId + " has deleted successfully.");
        return list(viewName, type);
    }

    protected ModelAndView history(String viewName, Integer docId) {
        DocHistory docHistory = docService.getDocHistory(docId);
        Doc doc = docService.get(docId);
        ModelAndView result = new ModelAndView(viewName);
        result.addObject("doc", doc);
        result.addObject("docHistory", docHistory);
        return result;
    }

    protected ModelAndView view(String viewName) {
        ModelAndView result = new ModelAndView(viewName);
        return result;
    }

    protected ModelAndView createPost(String viewName, Doc.Type type, HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);

        String title = req.getParameter("title");
        String format = req.getParameter("format");
        String contentText = req.getParameter("contentText");
        String btnAction = req.getParameter("btnAction");
        String path = req.getParameter("path");

        if (StringUtils.isEmpty(title) ||
                StringUtils.isEmpty(contentText) ||
                (type == Doc.Type.PAGE && StringUtils.isEmpty(path))) {
            req.setAttribute("title", title);
            req.setAttribute("contentText", contentText);
            req.setAttribute("format", format);
            req.setAttribute("path", path);

            req.setAttribute("actionErrorMessage", "Invalid inputs");
            return view(viewName);
        }

        Doc doc = DataUtils.createDoc(type, Content.Format.valueOf(format),
                userSession.getUser().getUsername(), title, contentText);
        docService.create(doc);
        String message = "Doc " + doc.getDocId() + " created successfully.";

        if ("publish".equals(btnAction)) {
            publishDoc(doc, doc.getLatestContent().getContentId(),
                    userSession.getUser().getUsername(), LocalDateTime.now());
            message += " And the content has published.";
        }

        req.setAttribute("actionSuccessMessage", message);
        return list(viewName, type);
    }

    protected ModelAndView edit(String viewName, Integer docId) {
        Doc doc = docService.get(docId);
        String ct = contentService.getContentText(doc.getLatestContent().getContentId());
        doc.getLatestContent().setContentText(ct);
        return edit(viewName, doc);
    }

    private ModelAndView edit(String viewName, Doc doc) {
        ModelAndView result = new ModelAndView(viewName);
        result.addObject("doc", doc);
        return result;
    }

    protected ModelAndView editPost(String viewName, Doc.Type type, HttpServletRequest req) {
        UserSession userSession = UserSessionUtils.getUserSession(req);

        Integer docId = Integer.parseInt(req.getParameter("docId"));
        String title = req.getParameter("title");
        String format = req.getParameter("format");
        String contentText = req.getParameter("contentText");
        String reasonForEdit = req.getParameter("reasonForEdit");
        String btnAction = req.getParameter("btnAction");
        String path = req.getParameter("path");

        Doc doc = docService.get(docId);
        doc.getLatestContent().setTitle(title);
        doc.getLatestContent().setCreatedUser(userSession.getUser().getUsername());
        doc.getLatestContent().setCreatedDt(LocalDateTime.now());
        doc.getLatestContent().setFormat(Content.Format.valueOf(format));
        doc.getLatestContent().setReasonForEdit(reasonForEdit);
        doc.getLatestContent().setContentText(contentText);

        if (StringUtils.isEmpty(title) ||
                StringUtils.isEmpty(contentText) ||
                (type == Doc.Type.PAGE && StringUtils.isEmpty(path))) {
            req.setAttribute("doc", doc);

            req.setAttribute("actionErrorMessage", "Invalid inputs");
            return edit(viewName, doc);
        }

        docService.update(doc);

        Integer contentId = doc.getLatestContent().getContentId();
        String message = "Doc " + docId + " with contentId " + contentId + " edited successfully.";

        if ("publish".equals(btnAction)) {
            publishDoc(doc, contentId, userSession.getUser().getUsername(), LocalDateTime.now());
            message += " And the content has published.";
        }

        req.setAttribute("actionSuccessMessage", message);

        return list(viewName, type);
    }

    protected ModelAndView preview(String viewName, Integer docId, Integer contentId) {
        // This get will serve as validation.
        Doc doc = docService.get(docId);

        Content content = contentService.get(contentId);
        String contentText = contentService.getContentHtml(content);

        ModelAndView result = new ModelAndView(viewName);
        result.addObject("doc", doc);
        result.addObject("contentText", contentText);
        return result;
    }
}
