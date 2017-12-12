package com.zemian.adocblog.web.listener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserSessionUtils {
    public static final String USER_SESSION_KEY = "userSession";

    public static UserSession getUserSession(HttpServletRequest req) {
        HttpSession httpSession = req.getSession(true);
        UserSession userSession = (UserSession)httpSession.getAttribute(USER_SESSION_KEY);

        if (userSession == null) {
            userSession = new UserSession();
            httpSession.setAttribute(USER_SESSION_KEY, userSession);
        }

        return userSession;
    }
}
