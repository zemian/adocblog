package com.zemian.adocblog.service;

import com.zemian.adocblog.cipher.PasswordHasher;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.dao.UserDAO;
import com.zemian.adocblog.data.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private static Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordHasher passwordHasher;

    @Autowired
    private UserDAO userDAO;

    public void create(User user) {
        LOG.debug("Hashing password for new user {}", user.getUsername());
        String plainPassword = user.getPassword();
        user.setPassword(passwordHasher.createHash(plainPassword));
        userDAO.create(user);
        LOG.debug("{} created", user);
    }

    public void update(User user) {
        userDAO.update(user);
    }

    public void markForDelete(String username) {
        userDAO.markForDelete(username);
    }

    public PagingList<User> findAll(Paging paging) {
        return userDAO.findAll(paging);
    }

    public boolean exists(String username) {
        return userDAO.exists(username);
    }

    public User get(String username) {
        return userDAO.get(username);
    }

    public void delete(String username) {
        userDAO.markForDelete(username);
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordHasher.verifyPassword(plainPassword, hashedPassword);
    }
}
