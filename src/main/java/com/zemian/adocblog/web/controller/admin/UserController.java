package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.User;
import com.zemian.adocblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    public ModelAndView list(Paging paging) {
        PagingList<User> users = userService.findAll(paging);
        ModelAndView result = new ModelAndView("/admin/users");
        result.addObject("users", users);
        return result;
    }
}
