package com.zemian.adocblog.service;

import com.zemian.adocblog.data.dao.AuditLogDAO;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AuditLogService {
    @Value("${app.service.AuditLogService.removeOldLogsPeriodInMonths}")
    private long removeOldLogsPeriodInMonths;

    @Autowired
    private AuditLogDAO auditLogDAO;

    public AuditLog create(String name, String value) {
        return auditLogDAO.create(name, value);
    }

    public void create(AuditLog auditLog) {
        auditLogDAO.create(auditLog);
    }

    public void update(AuditLog auditLog) {
        auditLogDAO.update(auditLog);
    }

    public AuditLog get(Integer logId) {
        return auditLogDAO.get(logId);
    }

    public void delete(Integer logId) {
        auditLogDAO.delete(logId);
    }

    public List<AuditLog> findAll() {
        return auditLogDAO.findAll();
    }

    public boolean exists(Integer logId) {
        return auditLogDAO.exists(logId);
    }

    public PagingList<AuditLog> find(Paging paging) {
        return auditLogDAO.find(paging);
    }

    public int removeOldLogs() {
        if (removeOldLogsPeriodInMonths <= 0) {
            return 0;
        }

        LocalDateTime sinceDt = LocalDateTime.now().minusMonths(removeOldLogsPeriodInMonths);
        int ret = auditLogDAO.removeOldLogs(sinceDt);
        if (ret > 0) {
            auditLogDAO.create("LOG_REMOVED",
                    "Deleted " + ret + " log entries older than " + sinceDt);
        }

        return ret;
    }
}
