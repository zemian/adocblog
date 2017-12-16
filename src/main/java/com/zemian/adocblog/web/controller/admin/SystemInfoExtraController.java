package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.AppException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Get extra system info. We will keep this URL unpublished in the menu due to sensitive data.
 */
@Controller
public class SystemInfoExtraController {
    @GetMapping("/admin/system-info-extra")
    public ModelAndView systemInfo() {
        Map<String, String> sysInfo = new TreeMap<>(getExtraSystemInfo());
        ModelAndView result = new ModelAndView("/admin/system-info");
        result.addObject("sysInfo", sysInfo);
        return result;
    }

    private Map<String, String> getExtraSystemInfo() {
        Map<String, String> result = new HashMap<>();
        
        Runtime runtime = Runtime.getRuntime();
        NumberFormat nf = DecimalFormat.getInstance();

        // System OS info
        try {
            result.put("xtra.system.hostname", InetAddress.getLocalHost().getHostName());
            result.put("xtra.system.ip", InetAddress.getLocalHost().getHostAddress());
            result.put("xtra.system.availableProcessors", "" + runtime.availableProcessors());
        } catch (Exception e) {
            throw new AppException("Failed ot get system info", e);
        }

        // Write memory available.
        result.put("xtra.jvm.maxMemory", nf.format(runtime.maxMemory()));
        result.put("xtra.jvm.totalMemory", nf.format(runtime.totalMemory()));
        result.put("xtra.jvm.freeMemory", nf.format(runtime.freeMemory()));

        // Physical memory
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            Object attribute = mBeanServer.getAttribute(new ObjectName("java.lang","type","OperatingSystem"), "TotalPhysicalMemorySize");
            long memSize = Long.parseLong(attribute.toString());
            result.put("xtra.system.physicalmemory", nf.format(memSize));
        } catch (Exception e) {
            // Ignore.
        }

        // Disk info
        File[] roots = File.listRoots();
        int index = 1;
        for (File root : roots) {
            result.put("xtra.filesystem.root." + index, root.getAbsolutePath());
            result.put("xtra.filesystem.root." + index + ".total.space", nf.format(root.getTotalSpace()));
            result.put("xtra.filesystem.root." + index + ".free.space", nf.format(root.getFreeSpace()));
        }

        // Get time related values
        result.put("xtra.time.currentdate", "" + new Date());
        result.put("xtra.time.currentmillis", "" + System.currentTimeMillis());
        result.put("xtra.time.currentnano", "" + System.nanoTime());

        // Get env vars
        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            result.put("xtra2.env" + entry.getKey(), entry.getValue());
        }
        return result;
    }
}
