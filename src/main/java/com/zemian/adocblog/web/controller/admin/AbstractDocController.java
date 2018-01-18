package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.domain.DocHistory;
import com.zemian.adocblog.service.ContentService;
import com.zemian.adocblog.service.DocService;
import com.zemian.adocblog.web.controller.AbstractController;
import com.zemian.adocblog.web.listener.UserSession;
import com.zemian.adocblog.web.listener.UserSessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base class to support Doc related processing.
 */
public abstract class AbstractDocController extends AbstractController {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDocController.class);
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Value("${app.web.defaultPagingSize}")
    protected int defaultPagingSize;

    @Autowired
    protected DocService docService;

    @Autowired
    protected ContentService contentService;

    protected ModelAndView list(String viewName, Doc.Type type, Paging paging) {
        PagingList<Doc> docs = docService.findLatest(paging, type);
        return getView(viewName, "docs", docs);
    }

    protected ModelAndView handlePublish(String viewName,
                                         Integer docId, Integer contentId,
                                         HttpServletRequest req,
                                         RedirectAttributes redirectAttributes) {
        UserSession userSession = UserSessionUtils.getUserSession(req);
        Doc page = docService.get(docId);
        handlePublishDoc(page, contentId, userSession.getUser().getUsername(), LocalDateTime.now());

        redirectAttributes.addFlashAttribute("actionSuccessMessage",
                "Doc " + docId + " with contentId " + contentId + " has published successfully.");
        return getView("redirect:" + viewName);
    }

    private void handlePublishDoc(Doc doc, Integer contentId, String username, LocalDateTime publishedDt) {
        Content content = new Content();
        content.setContentId(contentId);
        doc.setLatestContent(content);
        doc.setPublishedUser(username);
        doc.setPublishedDt(publishedDt);

        docService.publish(doc);
        LOG.info("Doc {} with contentId {} is published by {}", doc.getDocId(), contentId, username);
    }

    protected ModelAndView handlePublishByDate(String viewName,
                                               Integer docId, Integer contentId, String publishDate,
                                               HttpServletRequest req,
                                               RedirectAttributes redirectAttributes) {
        if (publishDate.indexOf(":") < 0) {
            // Auto append time if it's missing
            publishDate += " 00:00";
        }

        UserSession userSession = UserSessionUtils.getUserSession(req);
        LocalDateTime publishedDt = LocalDateTime.parse(publishDate, YYYY_MM_DD_HH_MM);

        Doc doc = docService.get(docId);
        handlePublishDoc(doc, contentId, userSession.getUser().getUsername(), publishedDt);

        redirectAttributes.addFlashAttribute("actionSuccessMessage",
                "Doc " + docId + " with contentId " + contentId +
                        " has published successfully and publishedDate " + publishDate + ".");
        return getView("redirect:" + viewName);
    }

    protected ModelAndView handleUnpublish(String viewName,
                                           Integer docId,
                                           HttpServletRequest req,
                                           RedirectAttributes redirectAttributes) {
        UserSession userSession = UserSessionUtils.getUserSession(req);
        Doc doc = docService.get(docId);
        if (doc.getPublishedContent() == null) {
            throw new AppException("Doc " + docId + " is not yet published");
        }
        Integer contentId = doc.getPublishedContent().getContentId();
        docService.unpublish(docId);
        LOG.info("Doc {} with contentId {} unpublished by {}",
                docId, contentId, userSession.getUser().getUsername());

        redirectAttributes.addFlashAttribute("actionSuccessMessage",
                "Doc " + docId + " with contentId " + contentId + " has unpublished successfully.");
        return getView("redirect:" + viewName);
    }

    protected ModelAndView handleDelete(String viewName, Integer docId,
                                        HttpServletRequest req,
                                        RedirectAttributes redirectAttributes) {
        String reasonForDelete = null;
        docService.markForDelete(docId, reasonForDelete);

        redirectAttributes.addFlashAttribute("actionSuccessMessage", "Doc " + docId + " has deleted successfully.");
        return getView("redirect:" + viewName);
    }

    protected ModelAndView handleHistory(String viewName, Integer docId) {
        DocHistory docHistory = docService.getDocHistory(docId);
        Doc doc = docService.get(docId);
        return getView(viewName, "doc", doc, "docHistory", docHistory);
    }

    protected boolean validDoc(Doc doc, BindingResult bindingResult) {
        ValidationUtils.rejectIfEmpty(bindingResult, "latestContent.title", "doc.latestContent.title", "Title cannot be empty");
        ValidationUtils.rejectIfEmpty(bindingResult, "latestContent.contentText", "doc.latestContent.contentText", "Content cannot be empty");
        if (doc.getType() == Doc.Type.PAGE) {
            ValidationUtils.rejectIfEmpty(bindingResult, "path", "doc.path", "Path cannot be empty");
        }
        return !bindingResult.hasErrors();
    }

    protected ModelAndView handleCreateSubmit(String viewName,
                                              Doc doc,
                                              HttpServletRequest req,
                                              RedirectAttributes redirectAttrs) {
        if (StringUtils.isEmpty(doc.getPath())) {
            doc.setPath(null);
        }
        UserSession userSession = UserSessionUtils.getUserSession(req);
        doc.getLatestContent().setCreatedUser(userSession.getUser().getUsername());
        doc.getLatestContent().setCreatedDt(LocalDateTime.now());
        doc.getLatestContent().setVersion(1);

        docService.create(doc);
        String message = "Doc " + doc.getDocId() + " created successfully.";

        String btnAction = req.getParameter("btnAction");
        if ("publish".equals(btnAction)) {
            handlePublishDoc(doc, doc.getLatestContent().getContentId(),
                    userSession.getUser().getUsername(), LocalDateTime.now());
            message += " And the content has published.";
        }

        redirectAttrs.addFlashAttribute("actionSuccessMessage", message);
        return getView("redirect:" + viewName);
    }

    protected ModelAndView handleEditView(String viewName, Integer docId) {
        Doc doc = docService.get(docId);
        String ct = contentService.getContentText(doc.getLatestContent().getContentId());
        doc.getLatestContent().setContentText(ct);
        return getView(viewName, "doc", doc);
    }

    protected ModelAndView handlEditSubmit(String viewName,
                                           Doc doc,
                                           HttpServletRequest req,
                                           RedirectAttributes redirectAttrs) {
        UserSession userSession = UserSessionUtils.getUserSession(req);
        Doc existingDoc = docService.get(doc.getDocId());
        existingDoc.setTags(doc.getTags());

        if (existingDoc.getType() == Doc.Type.PAGE) {
            existingDoc.setPath(doc.getPath());
        }

        doc.getLatestContent().setVersion(existingDoc.getLatestContent().getVersion() + 1);
        existingDoc.setLatestContent(doc.getLatestContent());

        existingDoc.getLatestContent().setCreatedDt(LocalDateTime.now());
        existingDoc.getLatestContent().setCreatedUser(userSession.getUser().getUsername());

        docService.update(existingDoc);
        Integer contentId = existingDoc.getLatestContent().getContentId();
        String message = "Doc " + existingDoc.getDocId() + " with contentId " + contentId + " edited successfully.";

        String btnAction = req.getParameter("btnAction");
        if ("publish".equals(btnAction)) {
            handlePublishDoc(existingDoc, contentId, userSession.getUser().getUsername(), LocalDateTime.now());
            message += " And the content has published.";
        }

        redirectAttrs.addFlashAttribute("actionSuccessMessage", message);
        return getView("redirect:" + viewName);
    }

    protected ModelAndView handlePreview(String viewName, Integer docId, Integer contentId) {
        // This get will serve as validation.
        Doc doc = docService.get(docId);

        Content content = contentService.get(contentId);
        String contentText = contentService.getContentHtml(content);

        return getView(viewName, "doc", doc, "contentText", contentText);
    }
}
