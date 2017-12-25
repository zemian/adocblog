package com.zemian.adocblog.service;

import com.zemian.adocblog.data.dao.AuditLogDAO;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.dao.SettingDAO;
import com.zemian.adocblog.data.domain.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class SettingService {
    @Autowired
    private SettingDAO settingDAO;

    @Autowired
    protected AuditLogDAO auditLogDAO;

    public PagingList<Setting> find(Paging paging) {
        return settingDAO.find(paging);
    }

    public void create(Setting setting) {
        settingDAO.create(setting);
    }

    public Setting get(Integer settingId) {
        return settingDAO.get(settingId);
    }

    public void delete(Integer settingId) {
        settingDAO.delete(settingId);
        auditLogDAO.create("SETTING_DELETED", "SettingId=" + settingId);
    }

    public void update(Setting setting) {
        settingDAO.update(setting);
        auditLogDAO.create("SETTING_UPDATED", "" + setting);
    }

    public List<Setting> findByCategory(String category) {
        return settingDAO.findByCategory(category);
    }

    public Map<String, Setting> getCategoryMap(String category) {
        return settingDAO.findByCategory(category).stream().collect(
                Collectors.toMap(Setting::getName, Function.identity()));
    }

    public int count() {
        return settingDAO.count();
    }

    public boolean exists(String category, String name) {
        return settingDAO.exists(category, name);
    }
}
