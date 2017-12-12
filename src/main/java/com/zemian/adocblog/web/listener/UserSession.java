package com.zemian.adocblog.web.listener;

import com.zemian.adocblog.data.domain.User;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id = UUID.randomUUID().toString();
    private Date loginDate = new Date();
    private User user;
    private String loginSuccessUrl;

    public UserSession() { }

    public String getLoginSuccessUrl() {
        return loginSuccessUrl;
    }

    public void setLoginSuccessUrl(String loginSuccessUrl) {
        this.loginSuccessUrl = loginSuccessUrl;
    }

    public String getId() {
        return id;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "id='" + id + '\'' +
                ", loginDate=" + loginDate +
                ", user=" + user +
                '}';
    }
}
