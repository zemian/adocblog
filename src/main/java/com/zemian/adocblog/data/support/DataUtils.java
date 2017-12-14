package com.zemian.adocblog.data.support;

import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.domain.Doc;

import java.time.LocalDateTime;

public class DataUtils {
    public static Doc createDoc(Doc.Type type, Content.Format format, String username, String title, String contentText) {
        Content content = createContent(format, username, title, contentText);
        Doc doc = new Doc();
        doc.setType(type);
        doc.setLatestContent(content);

        return doc;
    }

    public static Content createContent(Content.Format format, String username, String title, String contentText) {
        Content content = new Content();
        content.setVersion(1);
        content.setTitle(title);
        content.setFormat(format);
        content.setCreatedUser(username);
        content.setCreatedDt(LocalDateTime.now());
        content.setContentText(contentText);
        return content;
    }
}
