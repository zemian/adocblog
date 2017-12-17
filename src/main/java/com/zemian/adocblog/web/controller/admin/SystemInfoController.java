package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.support.AppUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Get server system and java information. Use "full=true" flag to get full system properties.
 */
@Controller
public class SystemInfoController {
    @GetMapping("/admin/system-info")
    public ModelAndView systemInfo(@RequestParam(defaultValue = "false") boolean full) {
        Map<String, String> sysInfo = new TreeMap<>();
        Properties sysProps = System.getProperties();
        if (full) {
            // Get full system props
            for (String name : sysProps.stringPropertyNames()) {
                sysInfo.put(name, sysProps.getProperty(name));
            }
        } else {
            // Get only small set of system props
            for (String name : sysProps.stringPropertyNames()) {
                if (name.startsWith("os") || name.startsWith("java.vm.specification")) {
                    sysInfo.put(name, sysProps.getProperty(name));
                }
            }
        }

        // Add release info
        Properties relProps = AppUtils.getReleaseProps();
        for (String name : relProps.stringPropertyNames()) {
            sysInfo.put("adocblog." + name, relProps.getProperty(name));
        }

        ModelAndView result = new ModelAndView("/admin/system-info");
        result.addObject("sysInfo", sysInfo);
        return result;
    }
}
