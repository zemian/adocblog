package com.zemian.adocblog.service;

import com.zemian.adocblog.SpringTestBase;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.support.DataUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = ServiceConfig.class)
public class ContentServiceTest extends SpringTestBase {

    @Autowired
    private ContentService contentService;

    @Test
    public void crud() {
        // Create
        Content content = DataUtils.createContent(
                Content.Format.ADOC, "test", "test doc", "ContentService *test*");
        contentService.create(content);

        try {
            // Get
            Content content2 = contentService.get(content.getContentId());

            // Get content text
            String ct = contentService.getContentText(content.getContentId());
            assertThat(ct, is("ContentService *test*"));
        } finally {
            contentService.delete(content.getContentId());
        }
    }


    @Test
    public void getContentHtml() {
        // Create
        Content content = DataUtils.createContent(
                Content.Format.ADOC, "test", "test doc", "ContentService *test*");
        contentService.create(content);

        try {
            // Get formatted content text
            Content content2 = contentService.get(content.getContentId());
            String ct = contentService.getContentHtml(content2);
            assertThat(ct, is("<div class=\"paragraph\">\n<p>ContentService <strong>test</strong></p>\n</div>"));
        } finally {
            contentService.delete(content.getContentId());
        }
    }
}