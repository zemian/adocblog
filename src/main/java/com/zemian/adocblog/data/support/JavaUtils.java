package com.zemian.adocblog.data.support;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JavaUtils {
    public static final DateTimeFormatter DATETIME_FROMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
    public static Date toDate(LocalDateTime dt) {
        return Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static String formatDate(LocalDateTime dt) {
        return formatDate(dt, DATETIME_FROMATTER);
    }
    public static String formatDate(LocalDateTime dt, DateTimeFormatter format) {
        return dt.format(format);
    }

    public static Map<String, Object> map(Object ... items) {
        Map<String, Object> ret = new HashMap<>();
        for (int i = 0; i < items.length; i+= 2) {
            ret.put(items[0].toString(), items[i + 1]);
        }
        return ret;
    }
}
