package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.AuditLog;
import com.zemian.adocblog.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuditLogController extends AbstractController {
    @Autowired
    private AuditLogService auditLogService;

    @GetMapping({"/admin/audit-log/list", "/admin/audit-log"})
    public ModelAndView list(Paging paging) {
        PagingList<AuditLog> plist = auditLogService.find(paging);
        return getView("/admin/audit-log/list", "plist", plist);
    }

    @GetMapping("/admin/audit-log/detail/{logId}")
    public ModelAndView detail(@PathVariable Integer logId) {
        AuditLog auditLog = auditLogService.get(logId);
        return getView("/admin/audit-log/detail", "auditLog", auditLog);
    }
}
