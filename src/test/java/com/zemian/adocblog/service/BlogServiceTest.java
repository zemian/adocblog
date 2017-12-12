package com.zemian.adocblog.service;

import com.zemian.adocblog.BaseSpringTest;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.domain.Blog;
import com.zemian.adocblog.data.domain.BlogHistory;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.support.DocUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ContextConfiguration(classes = ServiceConfig.class)
public class BlogServiceTest extends BaseSpringTest {
    @Autowired
    private BlogService blogService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    public void crud() {
        // Create
        Blog blog = DocUtils.createDoc(
                "test", "Just a test", "ADOC", "BlogServiceTest *test*");
        blogService.create(blog);

        try {
            // Get
            Blog blog2 = blogService.get(blog.getBlogId());
            assertThat(blog2.getBlogId(), greaterThanOrEqualTo(1));
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

            // Get content text
            String ct = contentService.getContentText(blog2.getLatestContent().getContentId());
            assertThat(ct, is("BlogServiceTest *test*"));

            // Get formatted content text
            ct = contentService.getContentHtml(blog2.getLatestContent());
            assertThat(ct, is("<div class=\"paragraph\">\n<p>BlogServiceTest <strong>test</strong></p>\n</div>"));

            // Get Latest List
            final Integer contentId = blog2.getLatestContent().getContentId();
            List<Blog> list = blogService.findLatest(new Paging()).getList();
            assertThat(list.size(), greaterThanOrEqualTo(1));
            Optional<Blog> found = list.stream().
                    filter(b -> b.getLatestContent().getContentId() == contentId).findFirst();
            assertThat(found.isPresent(), is(true));

            // Get Published List
            list = blogService.findPublished(new Paging()).getList();
            found = list.stream().
                    filter(b -> b.getLatestContent().getContentId() == contentId).findFirst();
            assertThat(found.isPresent(), is(false));

            // Publish
            blog2.setPublishedUser("test");
            blog2.setPublishedDt(LocalDateTime.now());
            blogService.publish(blog2);
            blog2 = blogService.get(blog.getBlogId());
            assertThat(blog2.getBlogId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getTitle(), is("Just a test"));
            assertThat(blog2.getLatestContent().getContentId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getVersion(), is(1));
            assertThat(blog2.getLatestContent().getReasonForEdit(), nullValue());
            assertThat(blog2.getLatestContent().getFormat(), is(Content.Format.ADOC));
            assertThat(blog2.getLatestContent().getCreatedUser(), is("test"));
            assertThat(blog2.getLatestContent().getCreatedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(blog2.getPublishedContent(), notNullValue());
            assertThat(blog2.getPublishedUser(), is("test"));
            assertThat(blog2.getPublishedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(blog2.getPublishedContent().getTitle(), is("Just a test"));
            assertThat(blog2.getPublishedContent().getContentId(), is(blog2.getLatestContent().getContentId()));
            assertThat(blog2.getPublishedContent().getVersion(), is(1));
            assertThat(blog2.getPublishedContent().getReasonForEdit(), nullValue());
            assertThat(blog2.getPublishedContent().getFormat(), is(Content.Format.ADOC));
            assertThat(blog2.getPublishedContent().getCreatedUser(), is("test"));
            assertThat(blog2.getPublishedContent().getCreatedDt(), lessThanOrEqualTo(LocalDateTime.now()));

            // Update content
            ct = "_Version 2_";
            blog2.getLatestContent().setContentText(ct);
            blog2.getLatestContent().setReasonForEdit("test edit");
            blogService.update(blog2);
            blog2 = blogService.get(blog.getBlogId());
            assertThat(blog2.getBlogId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getTitle(), is("Just a test"));
            assertThat(blog2.getLatestContent().getContentId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getContentId(), not(contentId));
            assertThat(blog2.getLatestContent().getVersion(), is(2));
            assertThat(blog2.getLatestContent().getReasonForEdit(), is("test edit"));
            assertThat(blog2.getLatestContent().getFormat(), is(Content.Format.ADOC));
            assertThat(blog2.getLatestContent().getCreatedUser(), is("test"));
            assertThat(blog2.getLatestContent().getCreatedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(blog2.getPublishedContent(), notNullValue());
            assertThat(blog2.getPublishedContent().getTitle(), is("Just a test"));
            assertThat(blog2.getPublishedContent().getContentId(), is(contentId));
            assertThat(blog2.getPublishedContent().getContentId(), not(blog2.getLatestContent().getContentId()));
            assertThat(blog2.getPublishedContent().getVersion(), is(1));

            // Get content text after publish + update
            ct = contentService.getContentText(blog2.getLatestContent().getContentId());
            assertThat(ct, is("_Version 2_"));

            // Get Latest List - after publish
            final Integer contentId2 = blog2.getLatestContent().getContentId();
            list = blogService.findLatest(new Paging()).getList();
            assertThat(list.size(), greaterThanOrEqualTo(1));
            found = list.stream().
                    filter(b -> b.getLatestContent().getContentId() == contentId2).findFirst();
            assertThat(found.isPresent(), is(true));

            // Get Published List after publish + update
            list = blogService.findPublished(new Paging()).getList();
            found = list.stream().
                    filter(b -> b.getLatestContent().getContentId() == contentId2).findFirst();
            assertThat(found.isPresent(), is(true));

            // Get history
            BlogHistory bh = blogService.getBlogHistory(blog2.getBlogId());
            assertThat(bh.getBlogId(), is(blog2.getBlogId()));
            assertThat(bh.getPublishedContentId(), is(contentId));
            assertThat(bh.getContentVers().size(), is(2));

            List<Integer> cIds = bh.getContentVers().stream().map(c -> c.getContentId()).collect(Collectors.toList());
            assertThat(cIds, hasItems(contentId, contentId2));

            // Unpublish
            blogService.unpublish(blog2.getBlogId());
            blog2 = blogService.get(blog.getBlogId());
            assertThat(blog2.getPublishedContent(), nullValue());

            // Delete
            blogService.delete(blog.getBlogId(), "delete for test");
            try {
                blogService.get(blog.getBlogId());
                Assert.fail("Blog " + blog.getBlogId() + " should not exists after delete.");
            } catch (RuntimeException e) {
                // Expecting
            }
        } finally {
            // delete blog for real so test can be clean
            blogService.deleteReal(blog.getBlogId());
        }
    }

    private Blog createAndPublish(String username, String format, String subject, String content) {
        return createAndPublish(username, format, subject, content, LocalDateTime.now());
    }

    private Blog createAndPublish(String username, String format, String subject, String content, LocalDateTime pubDate) {
        Blog blog = DocUtils.createDoc(username, subject, format, content);
        blogService.create(blog);

        blog.setPublishedDt(pubDate);
        blogService.publish(blog);

        return blog;
    }


    @Test
    public void createSamples() throws Exception {
        List<Blog> blogs = blogService.findLatest(new Paging(0, 1000)).getList();
        boolean sampleExists = blogs.stream().anyMatch(b -> b.getLatestContent().getTitle().equals("A asciidoc test"));
        if (!sampleExists) {
            createAndPublish("test", "ADOC", "A asciidoc test", "== Test me\n\nHello World!\n\n* one\n* two\n");
            createAndPublish("test", "HTML", "A html test", "<ul><li>one</li><li>two</li><li>three</li></ul>");
            createAndPublish("test", "TXT", "A text test", "print('Python is cool')");

            Blog blog = createAndPublish("test", "ADOC", "A asciidoc test with unpublish", "Writing AsciiDoc is _easy_!");
            blogService.unpublish(blog.getBlogId());

            File file = new File("readme.adoc");
            String readmeADoc = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            for (int i = 0; i < 1000; i++) {
                String subject = "ADocBlog readme";
                if (i > 0) {
                    subject += " - copy#" + i;
                }
                LocalDateTime dt = LocalDateTime.now().minusDays(i);
                createAndPublish("test", "ADOC", subject, readmeADoc, dt);
            }
        }
    }

}