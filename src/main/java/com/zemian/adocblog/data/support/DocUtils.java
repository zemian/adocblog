package com.zemian.adocblog.data.support;

import com.zemian.adocblog.data.domain.Blog;
import com.zemian.adocblog.data.domain.Content;

import java.time.LocalDateTime;

public class DocUtils {
    public static Blog createDoc(String username, String title, String format, String contentText) {
        Content c = new Content();
        c.setVersion(1);
        c.setTitle(title);
        c.setFormat(Content.Format.valueOf(format));
        c.setCreatedUser(username);
        c.setCreatedDt(LocalDateTime.now());
        c.setContentText(contentText);

        Blog doc = new Blog();
        doc.setLatestContent(c);

        return doc;
    }
}
