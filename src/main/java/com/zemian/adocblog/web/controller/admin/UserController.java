package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.data.dao.*;
import com.zemian.adocblog.data.domain.User;
import com.zemian.adocblog.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController extends AbstractController {
    @Autowired
    private UserService userService;

    @GetMapping({"/admin/user/list", "/admin/user"})
    public ModelAndView list(Paging paging) {
        PagingList<User> plist = userService.find(paging);
        return getView("/admin/user/list", "plist", plist);
    }

    @GetMapping("/admin/user/detail/{username}")
    public ModelAndView detail(@PathVariable String username) {
        User user = userService.get(username);
        return getView("/admin/user/detail", "user", user);
    }

    private boolean valid(User user, BindingResult bindingResult) {
        ValidationUtils.rejectIfEmpty(bindingResult, "fullName", "user.fullName", "Full Name cannot be empty");
        ValidationUtils.rejectIfEmpty(bindingResult, "admin", "user.admin", "Admin cannot be empty");
        return !bindingResult.hasErrors();
    }

    @Controller
    public class CreateForm {
        @GetMapping("/admin/user/create")
        public ModelAndView create() {
            return getView("/admin/user/create", "user", new User());
        }

        @PostMapping("/admin/user/create")
        public ModelAndView createSubmit(@ModelAttribute User user,
                                         BindingResult bindingResult, RedirectAttributes redirectAttrs) {
            ValidationUtils.rejectIfEmpty(bindingResult, "password", "user.password", "Password cannot be empty");
            if (!valid(user, bindingResult)) {
                return getErrorView("/admin/user/create", bindingResult, "user", user);
            }

            userService.create(user);
            redirectAttrs.addFlashAttribute("message",
                    "New item added with ID=" + user.getUsername());
            return getView("redirect:/admin/user/list");
        }
    }

    @Controller
    public class EditForm {
        @GetMapping("/admin/user/edit/{username}")
        public ModelAndView update(@PathVariable String username) {
            User user = userService.get(username);
            // Remove password
            user.setPassword(null);
            return getView("/admin/user/edit", "user", user);
        }

        @PostMapping("/admin/user/edit")
        public ModelAndView updateSubmit(@ModelAttribute User user,
                                         BindingResult bindingResult, RedirectAttributes redirectAttrs) {
            if (!valid(user, bindingResult)) {
                return getErrorView("/admin/user/edit", bindingResult, "user", user);
            }

            userService.update(user);

            if (StringUtils.isNotEmpty(user.getPassword())) {
                userService.changePassword(user.getUsername(), user.getPassword());
            }

            redirectAttrs.addFlashAttribute("message",
                    "Item ID=" + user.getUsername() + " updated successfully");
            return getView("redirect:/admin/user/list");
        }

    }

    @Controller
    public class DeleteForm {
        @GetMapping("/admin/user/delete/{username}")
        public ModelAndView delete(@PathVariable String username) {
            User user = userService.get(username);
            return getView("/admin/user/delete", "user", user);
        }

        @PostMapping("/admin/user/delete")
        public ModelAndView deleteSubmit(@RequestParam String username, RedirectAttributes redirectAttrs) {
            // Simple get will ensure the ID param is valid
            userService.get(username);

            userService.delete(username);
            redirectAttrs.addFlashAttribute("message",
                    "Item ID=" + username + " deleted successfully");
            return getView("redirect:/admin/user/list");
        }
    }
}
