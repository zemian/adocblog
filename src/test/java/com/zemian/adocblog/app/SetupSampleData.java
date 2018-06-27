package com.zemian.adocblog.app;

import com.zemian.adocblog.CommonConfig;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.domain.User;
import com.zemian.adocblog.data.support.DataUtils;
import com.zemian.adocblog.service.BlogService;
import com.zemian.adocblog.service.DocService;
import com.zemian.adocblog.service.ServiceConfig;
import com.zemian.adocblog.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class SetupSampleData {
    @Configuration
    @Import({ServiceConfig.class, CommonConfig.class})
    public static class Config {
        @Bean
        public SetupSampleData setupSampleData() {
            return new SetupSampleData();
        }
    }

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserService userService;

    @Autowired
    private DocService docService;

    @Autowired
    private BlogService blogService;

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext spring = new AnnotationConfigApplicationContext(SetupSampleData.Config.class);
        SetupSampleData main = spring.getBean(SetupSampleData.class);
        main.setupUsers();
        main.setupSampleDocs();
        main.setupSampleBlogs();
        spring.close();
    }

    public void setupUsers() {
        String userPrefix = "test";
        for (int i = 0; i < 5; i++) {
            String username = userPrefix + "_admin_" + i;
            if (!userService.exists(username)) {
                User user = new User(username, "test", true, "Test Admin");
                userService.create(user);
            }
        }

        for (int i = 0; i < 100; i++) {
            String username = userPrefix + "_" + i;
            if (!userService.exists(username)) {
                User user = new User(username, "test", false, "Test User");
                userService.create(user);
            }
        }
    }


    private Doc createAndPublish(Content.Format format, String username, String title, String content) {
        return createAndPublish(format, username, title, content, LocalDateTime.now());
    }

    private Doc createAndPublish(Content.Format format, String username, String title, String content, LocalDateTime pubDate) {
        Doc doc = DataUtils.createDoc(Doc.Type.PAGE, format, username, title, content);
        docService.create(doc);

        doc.setPublishedDt(pubDate);
        doc.setPublishedUser(username);
        docService.publish(doc);

        return doc;
    }

    public void setupSampleDocs() throws Exception {
        String titlePrefix = "Sample doc";
        String sql = "select count(*) from contents where title like ?";
        int queryCount = jdbc.queryForObject(sql, Integer.class, titlePrefix + "%");
        if (queryCount <= 0) {
            File file = new File("readme.adoc");
            String readmeADoc = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            int sampleSize = 100;
            for (int i = 0; i < sampleSize; i++) {
                String title = titlePrefix + " - copy#" + i;
                LocalDateTime dt = LocalDateTime.now().minusDays(i);
                createAndPublish(Content.Format.TEXT, "admin", title, readmeADoc, dt);
            }
        }
    }


    private Doc createAndPublishBlog(Content.Format format, String username, String title, String content) {
        return createAndPublishBlog(format, username, title, content, LocalDateTime.now());
    }

    private Doc createAndPublishBlog(Content.Format format, String username, String title, String content, LocalDateTime pubDate) {
        Doc doc = DataUtils.createDoc(Doc.Type.BLOG, format,
                username, title, content);
        blogService.create(doc);

        doc.setPublishedDt(pubDate);
        doc.setPublishedUser(username);
        blogService.publish(doc);

        return doc;
    }

    public void setupSampleBlogs() throws Exception {
        String titlePrefix = "Sample blog";
        String sql = "select count(*) from contents where title like ?";
        int queryCount = jdbc.queryForObject(sql, Integer.class, titlePrefix + "%");
        if (queryCount <= 0) {
            createAndPublishBlog(Content.Format.ADOC, "admin", titlePrefix + " - A asciidoc page sample", "== Test me\n\nHello World!\n\n* one\n* two\n");
            createAndPublishBlog(Content.Format.HTML, "admin", titlePrefix + " - A html page sample", "<ul><li>one</li><li>two</li><li>three</li></ul>");
            createAndPublishBlog(Content.Format.ADOC, "admin", titlePrefix + " - A asciidoc page sample2", "`print('Python is cool')`");

            Doc doc = createAndPublishBlog(Content.Format.ADOC, "admin", titlePrefix + " - A asciidoc test with unpublish", "Writing AsciiDoc is _easy_!");
            blogService.unpublish(doc.getDocId());

            File file = new File("readme.adoc");
            String readmeADoc = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            int sampleSize = 3_000;
            for (int i = 0; i < sampleSize; i++) {
                String title = titlePrefix + " - copy#" + i;
                LocalDateTime dt = LocalDateTime.now().minusDays(i);
                createAndPublishBlog(Content.Format.ADOC, "admin", title, readmeADoc, dt);
            }

            String text = FileUtils.readFileToString(new File("docs/release.adoc"), StandardCharsets.UTF_8);
            createAndPublishBlog(Content.Format.ADOC, "admin", titlePrefix + " - Release Notes", text);

            text = FileUtils.readFileToString(new File("docs/developer.adoc"), StandardCharsets.UTF_8);
            createAndPublishBlog(Content.Format.ADOC, "admin", titlePrefix + " - Developer Guide", text);

            text = FileUtils.readFileToString(new File("docs/userguide.adoc"), StandardCharsets.UTF_8);
            createAndPublishBlog(Content.Format.ADOC, "admin", titlePrefix + " - User Guide", text);
        }
    }
}
