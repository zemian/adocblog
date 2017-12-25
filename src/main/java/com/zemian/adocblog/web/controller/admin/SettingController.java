package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Setting;
import com.zemian.adocblog.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SettingController {
    @Autowired
    private SettingService settingService;

    @GetMapping("/admin/settings")
    public ModelAndView list(Paging paging) {
        PagingList<Setting> settings = settingService.findAll(paging);
        ModelAndView result = new ModelAndView("/admin/settings");
        result.addObject("settings", settings);
        return result;
    }
}
