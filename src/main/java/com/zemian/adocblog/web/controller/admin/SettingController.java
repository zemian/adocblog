package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.data.dao.*;
import com.zemian.adocblog.data.domain.Setting;
import com.zemian.adocblog.service.*;
import com.zemian.adocblog.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingController extends AbstractController {
    @Autowired
    private SettingService settingService;

    @GetMapping({"/admin/setting/list", "/admin/setting"})
    public ModelAndView list(Paging paging) {
        PagingList<Setting> plist = settingService.find(paging);
        return getView("/admin/setting/list", "plist", plist);
    }

    @GetMapping("/admin/setting/detail/{settingId}")
    public ModelAndView detail(@PathVariable Integer settingId) {
        Setting setting = settingService.get(settingId);
        return getView("/admin/setting/detail", "setting", setting);
    }

    private boolean valid(Setting setting, BindingResult bindingResult) {
        ValidationUtils.rejectIfEmpty(bindingResult, "category", "setting.category", "Category cannot be empty");
        ValidationUtils.rejectIfEmpty(bindingResult, "name", "setting.name", "Name cannot be empty");
        ValidationUtils.rejectIfEmpty(bindingResult, "value", "setting.value", "Value cannot be empty");
        ValidationUtils.rejectIfEmpty(bindingResult, "type", "setting.type", "Type cannot be empty");
        return !bindingResult.hasErrors();
    }

    @Controller
    public class CreateForm {
        @GetMapping("/admin/setting/create")
        public ModelAndView create() {
            return getView("/admin/setting/create", "setting", new Setting());
        }

        @PostMapping("/admin/setting/create")
        public ModelAndView createSubmit(@ModelAttribute Setting setting,
                                         BindingResult bindingResult, RedirectAttributes redirectAttrs) {
            if (!valid(setting, bindingResult)) {
                return getErrorView("/admin/setting/create", bindingResult, "setting", setting);
            }

            settingService.create(setting);
            redirectAttrs.addFlashAttribute("message",
                    "New item added with ID=" + setting.getSettingId());
            return getView("redirect:/admin/setting/list");
        }
    }

    @Controller
    public class EditForm {
        @GetMapping("/admin/setting/edit/{settingId}")
        public ModelAndView update(@PathVariable Integer settingId) {
            Setting setting = settingService.get(settingId);
            return getView("/admin/setting/edit", "setting", setting);
        }

        @PostMapping("/admin/setting/edit")
        public ModelAndView updateSubmit(@ModelAttribute Setting setting,
                                         BindingResult bindingResult, RedirectAttributes redirectAttrs) {
            if (!valid(setting, bindingResult)) {
                return getErrorView("/admin/setting/edit", bindingResult, "setting", setting);
            }

            settingService.update(setting);
            redirectAttrs.addFlashAttribute("message",
                    "Item ID=" + setting.getSettingId() + " updated successfully");
            return getView("redirect:/admin/setting/list");
        }

    }

    @Controller
    public class DeleteForm {
        @GetMapping("/admin/setting/delete/{settingId}")
        public ModelAndView delete(@PathVariable Integer settingId) {
            Setting setting = settingService.get(settingId);
            return getView("/admin/setting/delete", "setting", setting);
        }

        @PostMapping("/admin/setting/delete")
        public ModelAndView deleteSubmit(@RequestParam Integer settingId, RedirectAttributes redirectAttrs) {
            // Simple get will ensure the ID param is valid
            settingService.get(settingId);

            settingService.delete(settingId);
            redirectAttrs.addFlashAttribute("message",
                    "Item ID=" + settingId + " deleted successfully");
            return getView("redirect:/admin/setting/list");
        }
    }
}
