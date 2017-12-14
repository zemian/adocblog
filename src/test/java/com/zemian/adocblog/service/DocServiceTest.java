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
        Doc doc = DataUtils.createDoc(Doc.Type.PAGE, Content.Format.ADOC,
                "test", "test doc", "DocServiceTest *test*");
        docService.create(doc);

        try {
            // Get
            Doc doc2 = docService.get(doc.getDocId());
            assertThat(doc2.getDocId(), greaterThanOrEqualTo(1));
            assertThat(doc2.getLatestContent().getTitle(), is("test doc"));
            assertThat(doc2.getLatestContent().getContentId(), greaterThanOrEqualTo(1));
            assertThat(doc2.getLatestContent().getVersion(), is(1));
            assertThat(doc2.getLatestContent().getReasonForEdit(), nullValue());
            assertThat(doc2.getLatestContent().getFormat(), is(Content.Format.ADOC));
            assertThat(doc2.getLatestContent().getCreatedUser(), is("test"));
            assertThat(doc2.getLatestContent().getCreatedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(doc2.getPublishedContent(), nullValue());
            assertThat(doc2.getPublishedUser(), nullValue());
            assertThat(doc2.getPublishedDt(), nullValue());
            assertThat(doc2.getType(), is(Doc.Type.PAGE));
            assertThat(doc2.getPath(), is("" + doc2.getDocId()));

            // Update content
            Integer oldDocId = doc2.getDocId();
            Integer contentId = doc2.getLatestContent().getContentId();
            String ct = "_Version 2_";
            doc2.getLatestContent().setContentText(ct);
            doc2.getLatestContent().setReasonForEdit("test edit");
            docService.update(doc2);
            doc2 = docService.get(doc.getDocId());
            assertThat(doc2.getDocId(), is(oldDocId));
            assertThat(doc2.getLatestContent().getTitle(), is("test doc"));
            assertThat(doc2.getLatestContent().getContentId(), greaterThanOrEqualTo(1));
            assertThat(doc2.getLatestContent().getContentId(), not(contentId));
            assertThat(doc2.getLatestContent().getVersion(), is(2));
            assertThat(doc2.getLatestContent().getReasonForEdit(), is("test edit"));
            assertThat(doc2.getLatestContent().getFormat(), is(Content.Format.ADOC));
            assertThat(doc2.getLatestContent().getCreatedUser(), is("test"));
            assertThat(doc2.getLatestContent().getCreatedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(doc2.getPublishedContent(), nullValue());
            assertThat(doc2.getPublishedUser(), nullValue());
            assertThat(doc2.getPublishedDt(), nullValue());
            assertThat(doc2.getType(), is(Doc.Type.PAGE));
            assertThat(doc2.getPath(), is("" + doc2.getDocId()));

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
    public void docPath() {
        // Create
        Doc doc = DataUtils.createDoc(Doc.Type.PAGE, Content.Format.ADOC,
                "test", "test doc", "DocServiceTest *test*");
        doc.setPath("/junit/test/test-doc");
        docService.create(doc);

        try {
            // Get
            Doc doc2 = docService.get(doc.getDocId());
            assertThat(doc2.getDocId(), greaterThanOrEqualTo(1));
            assertThat(doc2.getPath(), not("" + doc2.getDocId()));
            assertThat(doc2.getPath(), is("/junit/test/test-doc"));

            // Path should be unique
            try {
                Doc doc3 = DataUtils.createDoc(Doc.Type.PAGE, Content.Format.ADOC,
                        "test", "test doc2", "#2 DocServiceTest *test*");
                doc3.setPath("/junit/test/test-doc");
                docService.create(doc3);
                Assert.fail("Create doc should fail when path is duplicated.");
            } catch (Exception e) {
                // Expected.
            }

            // Update content
            Integer oldDocId = doc2.getDocId();
            doc2.setPath("/junit/test/test-doc-edit");
            doc2.getLatestContent().setContentText(doc.getLatestContent().getContentText());
            doc2.getLatestContent().setReasonForEdit("test edit only path");
            docService.update(doc2);
            assertThat(doc2.getDocId(), is(oldDocId));
            assertThat(doc2.getPath(), not("" + doc2.getDocId()));
            assertThat(doc2.getPath(), is("/junit/test/test-doc-edit"));
        } finally {
            docService.delete(doc.getDocId());
        }
    }

    @Test
    public void findList() throws Exception {
        // Create
        List<Doc> docs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Doc doc = DataUtils.createDoc(Doc.Type.PAGE, Content.Format.ADOC,
                    "test", "findList test", "DocServiceTest *test*");
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
            List<Doc> list = docService.findLatest(new Paging(), Doc.Type.PAGE).getList();
            list = list.stream().filter(b -> b.getLatestContent().getTitle().equals("findList test")).collect(Collectors.toList());
            assertThat(list.size(), is(10));
            list.forEach(doc -> assertThat(doc.getType(), is(Doc.Type.PAGE)));

            // Get Published List
            list = docService.findPublished(new Paging(), Doc.Type.PAGE).getList();
            list = list.stream().filter(b -> b.getLatestContent().getTitle().equals("findList test")).collect(Collectors.toList());
            assertThat(list.size(), is(5));
            list.forEach(doc -> assertThat(doc.getType(), is(Doc.Type.PAGE)));
        } finally {
            for (Doc doc : docs) {
                docService.delete(doc.getDocId());
            }
        }
    }

    private Doc createAndPublish(Content.Format format, String username, String title, String content) {
        return createAndPublish(format, username, title, content, LocalDateTime.now());
    }

    private Doc createAndPublish(Content.Format format, String username, String title, String content, LocalDateTime pubDate) {
        Doc doc = DataUtils.createDoc(Doc.Type.PAGE, format,
                username, title, content);
        docService.create(doc);

        doc.setPublishedDt(pubDate);
        docService.publish(doc);

        return doc;
    }

    @Test
    public void createSamples() throws Exception {
        int sampleSize = 30;
        List<Doc> docs = docService.findLatest(new Paging(0, sampleSize), Doc.Type.PAGE).getList();
        boolean sampleExists = docs.stream().anyMatch(b -> b.getLatestContent().getTitle().equals("A asciidoc page sample"));
        if (!sampleExists) {
            createAndPublish(Content.Format.ADOC, "test", "A asciidoc page sample", "== Test me\n\nHello World!\n\n* one\n* two\n");
            createAndPublish(Content.Format.HTML, "test", "A html page sample", "<ul><li>one</li><li>two</li><li>three</li></ul>");
            createAndPublish(Content.Format.ADOC, "test", "A asciidoc page sample2", "`print('Python is cool')`");

            Doc doc = createAndPublish(Content.Format.ADOC, "test", "A asciidoc test with unpublish", "Writing AsciiDoc is _easy_!");
            docService.unpublish(doc.getDocId());

            File file = new File("readme.adoc");
            String readmeADoc = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            for (int i = 0; i < sampleSize; i++) {
                String title = "ADocDoc readme page sample";
                if (i > 0) {
                    title += " - copy#" + i;
                }
                LocalDateTime dt = LocalDateTime.now().minusDays(i);
                createAndPublish(Content.Format.ADOC, "test", title, readmeADoc, dt);
            }
        }
    }

}