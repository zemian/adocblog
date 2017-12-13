package com.zemian.adocblog.data.support;

import com.zemian.adocblog.data.domain.Blog;
import com.zemian.adocblog.data.domain.Content;

import java.time.LocalDateTime;

public class DataUtils {
    public static Blog createBlog(String username, String title, String format, String contentText) {
        Content content = createContent(username, title, format, contentText);
        Blog doc = new Blog();
        doc.setLatestContent(content);

        return doc;
    }

    public static Content createContent(String username, String title, String format, String contentText) {
        Content content = new Content();
        content.setVersion(1);
        content.setTitle(title);
        content.setFormat(Content.Format.valueOf(format));
        content.setCreatedUser(username);
        content.setCreatedDt(LocalDateTime.now());
        content.setContentText(contentText);
        return content;
    }
}
