package com.zemian.adocblog.web.listener;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserSessionInterceptor extends HandlerInterceptorAdapter {
    private boolean checkIsUserAdmin;
    private String loginUrl;
    private String adminRestrictedUrl;

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public void setCheckIsUserAdmin(boolean checkIsUserAdmin) {
        this.checkIsUserAdmin = checkIsUserAdmin;
    }

    public void setAdminRestrictedUrl(String notAdminUrl) {
        this.adminRestrictedUrl = notAdminUrl;
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
        } else {
            if (checkIsUserAdmin && !(userSession.getUser().isAdmin())) {
                String contextPath = request.getContextPath();
                response.sendRedirect(contextPath + adminRestrictedUrl);
                return false;
            }
        }

        return true;
    }
}
