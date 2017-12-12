package com.zemian.adocblog.web.listener;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserSessionInterceptor extends HandlerInterceptorAdapter {
    private String loginUrl;

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserSession userSession = UserSessionUtils.getUserSession(request);
        if (userSession.getUser() == null) {
            // Save original request URL so after login, we will know where to redirect again.
            userSession.setLoginSuccessUrl(request.getRequestURI());
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + loginUrl);
            return false;
        }
        return true;
    }
}
