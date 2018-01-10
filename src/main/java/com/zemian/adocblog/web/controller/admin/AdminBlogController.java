package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.support.DataUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView publish(@PathVariable Integer blogId, @PathVariable Integer contentId,
                                HttpServletRequest req, RedirectAttributes redirectAttrs) {

        return handlePublish("/admin/blog/list", blogId, contentId, req, redirectAttrs);
    }

    @GetMapping("/admin/blog/publish/{blogId}/{contentId}/{publishDate}")
    public ModelAndView publishByDate(@PathVariable Integer blogId,
                                      @PathVariable Integer contentId,
                                      @PathVariable String publishDate,
                                HttpServletRequest req, RedirectAttributes redirectAttrs) {
        return handlePublishByDate("/admin/blog/list", blogId, contentId, publishDate, req, redirectAttrs);
    }

    @GetMapping("/admin/blog/unpublish/{blogId}")
    public ModelAndView unpublish(@PathVariable Integer blogId, HttpServletRequest req, RedirectAttributes redirectAttrs) {
        return handleUnpublish("/admin/blog/list", blogId, req, redirectAttrs);
    }

    @GetMapping("/admin/blog/delete/{blogId}")
    public ModelAndView delete(@PathVariable Integer blogId, HttpServletRequest req, RedirectAttributes redirectAttrs) {
        return handleDelete("/admin/blog/list", blogId, req, redirectAttrs);
    }

    @GetMapping("/admin/blog/history/{blogId}")
    public ModelAndView history(@PathVariable Integer blogId) {
        return handleHistory("/admin/blog/history", blogId);
    }

    @Controller
    public class CreateForm {
        @GetMapping("/admin/blog/create")
        public ModelAndView create() {
            return getView("/admin/blog/create", "doc", DataUtils.createEmptyDoc(Doc.Type.BLOG));
        }

        @PostMapping("/admin/blog/create")
        public ModelAndView createSubmit(
                HttpServletRequest req,
                RedirectAttributes redirectAttrs,
                @ModelAttribute Doc doc,
                BindingResult bindingResult) {
            if (!validDoc(doc, bindingResult)) {
                return getErrorView("/admin/blog/create", bindingResult, "doc", doc);
            }
            return handleCreateSubmit("/admin/blog/list", doc, req, redirectAttrs);
        }
    }

    @Controller
    public class EditForm {
        @GetMapping("/admin/blog/edit/{blogId}")
        public ModelAndView edit(@PathVariable Integer blogId) {
            return handleEditView("/admin/blog/edit", blogId);
        }

        @PostMapping("/admin/blog/edit")
        public ModelAndView editSubmit(
                HttpServletRequest req,
                RedirectAttributes redirectAttrs,
                @ModelAttribute Doc doc,
                BindingResult bindingResult) {
            if (!validDoc(doc, bindingResult)) {
                return getErrorView("/admin/blog/edit", bindingResult, "doc", doc);
            }
            return handlEditSubmit("/admin/blog/list", doc, req, redirectAttrs);
        }
    }

    @GetMapping("/admin/blog/preview/{blogId}/{contentId}")
    public ModelAndView preview(@PathVariable Integer blogId, @PathVariable Integer contentId) {
        return handlePreview("/admin/blog/preview", blogId, contentId);
    }
}
