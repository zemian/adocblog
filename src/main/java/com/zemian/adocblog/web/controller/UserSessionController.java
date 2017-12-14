package com.zemian.adocblog.web.controller;

import com.zemian.adocblog.data.domain.User;
import com.zemian.adocblog.service.UserService;
import com.zemian.adocblog.web.listener.UserSession;
import com.zemian.adocblog.web.listener.UserSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserSessionController {
    private static Logger LOG = LoggerFactory.getLogger(UserSessionController.class);

    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView result = new ModelAndView("/login");
        return result;
    }

    @PostMapping("/login")
    public ModelAndView loginSubmit(HttpServletRequest req) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        ModelAndView result = new ModelAndView("redirect:/index");
        boolean loginFailed = true;
        try {
            User user = userService.get(username);
            if (userService.verifyPassword(password, user.getPassword())) {
                UserSession userSesssion = UserSessionUtils.getUserSession(req);
                userSesssion.setUser(user);
                loginFailed = false;
                LOG.info("User {} logged in", username);

                if (userSesssion.getLoginSuccessUrl() != null) {
                    result.setViewName("redirect:" + userSesssion.getLoginSuccessUrl());
                }
            } else {
                LOG.trace("User {} login failed. Incorrect password.", username);
            }
        } catch (EmptyResultDataAccessException e) {
            LOG.trace("User {} login failed. User not found.", username);
        } catch (RuntimeException e) {
            LOG.trace("User {} login failed.", username, e);
        }

        if (loginFailed){
            result.setViewName("/login");
            result.addObject("loginError", "Invalid login!");
            result.addObject("username", username);
            result.addObject("password", password);
        }

        return result;
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest req) {
        ModelAndView result = new ModelAndView("redirect:/index");

        UserSession userSession = UserSessionUtils.getUserSession(req);
        if (userSession.getUser() != null) {
            req.getSession().invalidate();
            LOG.info("User {} logged out.", userSession.getUser().getUsername());
        }

        return result;
    }
}
