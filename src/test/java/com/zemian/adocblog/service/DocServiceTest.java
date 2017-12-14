package com.zemian.adocblog.service;

import com.zemian.adocblog.SpringTestBase;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.support.DataUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ContextConfiguration(classes = ServiceConfig.class)
public class DocServiceTest extends SpringTestBase {
    @Autowired
    private DocService docService;

    @Test
    public void crud() {
        // Create
        Doc doc = DataUtils.createDoc(
                "test", "Just a test", "ADOC", "DocServiceTest *test*");
        docService.create(doc);

        try {
            // Get
            Doc blog2 = docService.get(doc.getDocId());
            assertThat(blog2.getDocId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getTitle(), is("Just a test"));
            assertThat(blog2.getLatestContent().getContentId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getVersion(), is(1));
            assertThat(blog2.getLatestContent().getReasonForEdit(), nullValue());
            assertThat(blog2.getLatestContent().getFormat(), is(Content.Format.ADOC));
            assertThat(blog2.getLatestContent().getCreatedUser(), is("test"));
            assertThat(blog2.getLatestContent().getCreatedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(blog2.getPublishedContent(), nullValue());
            assertThat(blog2.getPublishedUser(), nullValue());
            assertThat(blog2.getPublishedDt(), nullValue());

            // Update content
            Integer contentId = blog2.getLatestContent().getContentId();
            String ct = "_Version 2_";
            blog2.getLatestContent().setContentText(ct);
            blog2.getLatestContent().setReasonForEdit("test edit");
            docService.update(blog2);
            blog2 = docService.get(doc.getDocId());
            assertThat(blog2.getDocId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getTitle(), is("Just a test"));
            assertThat(blog2.getLatestContent().getContentId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getContentId(), not(contentId));
            assertThat(blog2.getLatestContent().getVersion(), is(2));
            assertThat(blog2.getLatestContent().getReasonForEdit(), is("test edit"));
            assertThat(blog2.getLatestContent().getFormat(), is(Content.Format.ADOC));
            assertThat(blog2.getLatestContent().getCreatedUser(), is("test"));
            assertThat(blog2.getLatestContent().getCreatedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(blog2.getPublishedContent(), nullValue());
            assertThat(blog2.getPublishedUser(), nullValue());
            assertThat(blog2.getPublishedDt(), nullValue());

            // Delete
            docService.markForDelete(doc.getDocId(), "markForDelete for test");
            try {
                docService.get(doc.getDocId());
                Assert.fail("Doc " + doc.getDocId() + " should not exists after markForDelete.");
            } catch (RuntimeException e) {
                // Expecting
            }
        } finally {
            docService.delete(doc.getDocId());
        }
    }

    @Test
    public void findList() throws Exception {
        // Create
        List<Doc> docs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Doc doc = DataUtils.createDoc(
                    "test", "findList test", "ADOC", "DocServiceTest *test*");
            docService.create(doc);
            docs.add(doc);

            if (i % 2 == 0) {
                doc.setPublishedUser("test");
                doc.setPublishedDt(LocalDateTime.now().plusMinutes(i)); // Set published with gap on purpose for testing.
                docService.publish(doc);
            }

            // Ensure create in order to test find Next/Prev
            Thread.sleep(300);
        }

        try {
            // Get Latest List
            List<Doc> list = docService.findLatest(new Paging()).getList();
            list = list.stream().filter(b -> b.getLatestContent().getTitle().equals("findList test")).collect(Collectors.toList());
            assertThat(list.size(), is(10));

            // Get Published List
            list = docService.findPublished(new Paging()).getList();
            list = list.stream().filter(b -> b.getLatestContent().getTitle().equals("findList test")).collect(Collectors.toList());
            assertThat(list.size(), is(5));
        } finally {
            for (Doc doc : docs) {
                docService.delete(doc.getDocId());
            }
        }
    }

    private Doc createAndPublish(String username, String format, String subject, String content) {
        return createAndPublish(username, format, subject, content, LocalDateTime.now());
    }

    private Doc createAndPublish(String username, String format, String subject, String content, LocalDateTime pubDate) {
        Doc doc = DataUtils.createDoc(username, subject, format, content);
        docService.create(doc);

        doc.setPublishedDt(pubDate);
        docService.publish(doc);

        return doc;
    }

    @Ignore
    @Test
    public void createSamples() throws Exception {
        List<Doc> docs = docService.findLatest(new Paging(0, 1000)).getList();
        boolean sampleExists = docs.stream().anyMatch(b -> b.getLatestContent().getTitle().equals("A asciidoc test"));
        if (!sampleExists) {
            createAndPublish("test", "ADOC", "A asciidoc test", "== Test me\n\nHello World!\n\n* one\n* two\n");
            createAndPublish("test", "HTML", "A html test", "<ul><li>one</li><li>two</li><li>three</li></ul>");
            createAndPublish("test", "TXT", "A text test", "print('Python is cool')");

            Doc doc = createAndPublish("test", "ADOC", "A asciidoc test with unpublish", "Writing AsciiDoc is _easy_!");
            docService.unpublish(doc.getDocId());

            File file = new File("readme.adoc");
            String readmeADoc = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            for (int i = 0; i < 1000; i++) {
                String subject = "ADocDoc readme";
                if (i > 0) {
                    subject += " - copy#" + i;
                }
                LocalDateTime dt = LocalDateTime.now().minusDays(i);
                createAndPublish("test", "ADOC", subject, readmeADoc, dt);
            }
        }
    }

}