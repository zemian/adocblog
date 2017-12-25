package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.AuditLog;
import com.zemian.adocblog.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuditLogController {
    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/admin/audit-logs")
    public ModelAndView list(Paging paging) {
        PagingList<AuditLog> plist = auditLogService.find(paging);
        ModelAndView result = new ModelAndView("/admin/audit-logs");
        result.addObject("auditLogs", plist);
        return result;
    }

    @GetMapping("/admin/audit-logs/remove-old-logs")
    public ModelAndView removeOldLogs(HttpServletRequest req) {
        int removedCount = auditLogService.removeOldLogs();
        req.setAttribute("actionSuccessMessage", removedCount + " logs has been removed.");
        return list(new Paging());
    }
}
