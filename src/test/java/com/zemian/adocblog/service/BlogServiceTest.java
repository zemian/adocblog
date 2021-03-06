package com.zemian.adocblog.service;

import com.zemian.adocblog.SpringTestBase;
import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.domain.Content;
import com.zemian.adocblog.data.domain.Doc;
import com.zemian.adocblog.data.domain.DocHistory;
import com.zemian.adocblog.data.support.DataUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ContextConfiguration(classes = ServiceConfig.class)
public class BlogServiceTest extends SpringTestBase {
    @Autowired
    private BlogService blogService;

    @Autowired
    private ContentService contentService;

    @Test
    public void crud() {
        // Create
        Doc blog = DataUtils.createDoc(Doc.Type.BLOG, Content.Format.ADOC,
                "admin", "Just a test", "BlogServiceTest *test*");
        blogService.create(blog);

        try {
            // Get
            Doc blog2 = blogService.get(blog.getDocId());
            assertThat(blog2.getDocId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getTitle(), is("Just a test"));
            assertThat(blog2.getLatestContent().getContentId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getVersion(), is(1));
            assertThat(blog2.getLatestContent().getReasonForEdit(), nullValue());
            assertThat(blog2.getLatestContent().getFormat(), is(Content.Format.ADOC));
            assertThat(blog2.getLatestContent().getCreatedUser(), is("admin"));
            assertThat(blog2.getLatestContent().getCreatedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(blog2.getPublishedContent(), nullValue());
            assertThat(blog2.getPublishedUser(), nullValue());
            assertThat(blog2.getPublishedDt(), nullValue());
            assertThat(blog2.getType(), is(Doc.Type.BLOG));

            // Update content
            Integer contentId = blog2.getLatestContent().getContentId();
            String ct = "_Version 2_";
            blog2.getLatestContent().setContentText(ct);
            blog2.getLatestContent().setReasonForEdit("test edit");
            blogService.update(blog2);
            blog2 = blogService.get(blog.getDocId());
            assertThat(blog2.getDocId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getTitle(), is("Just a test"));
            assertThat(blog2.getLatestContent().getContentId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getContentId(), not(contentId));
            assertThat(blog2.getLatestContent().getVersion(), is(2));
            assertThat(blog2.getLatestContent().getReasonForEdit(), is("test edit"));
            assertThat(blog2.getLatestContent().getFormat(), is(Content.Format.ADOC));
            assertThat(blog2.getLatestContent().getCreatedUser(), is("admin"));
            assertThat(blog2.getLatestContent().getCreatedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(blog2.getPublishedContent(), nullValue());
            assertThat(blog2.getPublishedUser(), nullValue());
            assertThat(blog2.getPublishedDt(), nullValue());
            assertThat(blog2.getType(), is(Doc.Type.BLOG));

            // Delete
            blogService.markForDelete(blog.getDocId(), "markForDelete for test");
            try {
                blogService.get(blog.getDocId());
                Assert.fail("Doc " + blog.getDocId() + " should not exists after markForDelete.");
            } catch (RuntimeException e) {
                // Expecting
            }
        } finally {
            blogService.delete(blog.getDocId());
        }
    }

    @Test
    public void blogOperations() {
        // Create
        Doc blog = DataUtils.createDoc(Doc.Type.BLOG, Content.Format.ADOC,
                "admin", "Just a test", "BlogServiceTest *test*");
        blogService.create(blog);

        try {
            // Get content text
            Doc blog2 = blogService.get(blog.getDocId());
            String ct = contentService.getContentText(blog2.getLatestContent().getContentId());
            assertThat(ct, is("BlogServiceTest *test*"));

            // Get Latest List
            final int contentId = blog2.getLatestContent().getContentId();
            List<Doc> list = blogService.findLatest(new Paging()).getList();
            assertThat(list.size(), greaterThanOrEqualTo(1));
            Optional<Doc> found = list.stream().
                    filter(b -> b.getLatestContent().getContentId().intValue() == contentId).findFirst();
            assertThat(found.isPresent(), is(true));

            // Get Published List
            list = blogService.findPublished(new Paging()).getList();
            found = list.stream().
                    filter(b -> b.getLatestContent().getContentId().intValue() ==  contentId).findFirst();
            assertThat(found.isPresent(), is(false));

            // Publish
            blog2.setPublishedUser("admin");
            blog2.setPublishedDt(LocalDateTime.now());
            blogService.publish(blog2);
            blog2 = blogService.get(blog.getDocId());
            assertThat(blog2.getDocId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getTitle(), is("Just a test"));
            assertThat(blog2.getLatestContent().getContentId(), greaterThanOrEqualTo(1));
            assertThat(blog2.getLatestContent().getVersion(), is(1));
            assertThat(blog2.getLatestContent().getReasonForEdit(), nullValue());
            assertThat(blog2.getLatestContent().getFormat(), is(Content.Format.ADOC));
            assertThat(blog2.getLatestContent().getCreatedUser(), is("admin"));
            assertThat(blog2.getLatestContent().getCreatedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(blog2.getPublishedContent(), notNullValue());
            assertThat(blog2.getPublishedUser(), is("admin"));
            assertThat(blog2.getPublishedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(blog2.getPublishedContent().getTitle(), is("Just a test"));
            assertThat(blog2.getPublishedContent().getContentId(), is(blog2.getLatestContent().getContentId()));
            assertThat(blog2.getPublishedContent().getVersion(), is(1));
            assertThat(blog2.getPublishedContent().getReasonForEdit(), nullValue());
            assertThat(blog2.getPublishedContent().getFormat(), is(Content.Format.ADOC));
            assertThat(blog2.getPublishedContent().getCreatedUser(), is("admin"));
            assertThat(blog2.getPublishedContent().getCreatedDt(), lessThanOrEqualTo(LocalDateTime.now()));
            assertThat(blog2.getType(), is(Doc.Type.BLOG));

            // Update content
            ct = "_Version 2_";
            blog2.getLatestContent().setContentText(ct);
            blog2.getLatestContent().setReasonForEdit("test edit");
            blogService.update(blog2);
            blog2 = blogService.get(blog.getDocId());
            assertThat(blog2.getLatestContent().getContentId(), not(contentId));
            assertThat(blog2.getLatestContent().getVersion(), is(2));
            assertThat(blog2.getPublishedContent().getVersion(), is(1));
            assertThat(blog2.getType(), is(Doc.Type.BLOG));

            // Get content text after publish + update
            ct = contentService.getContentText(blog2.getLatestContent().getContentId());
            assertThat(ct, is("_Version 2_"));

            // Get Latest List - after publish
            final int contentId2 = blog2.getLatestContent().getContentId();
            list = blogService.findLatest(new Paging()).getList();
            assertThat(list.size(), greaterThanOrEqualTo(1));
            found = list.stream().
                    filter(b -> b.getLatestContent().getContentId().intValue() == contentId2).findFirst();
            assertThat(found.isPresent(), is(true));

            // Get Published List after publish + update
            list = blogService.findPublished(new Paging()).getList();
            found = list.stream().
                    filter(b -> b.getLatestContent().getContentId().intValue() == contentId2).findFirst();
            assertThat(found.isPresent(), is(true));

            // Get history
            DocHistory bh = blogService.getDocHistory(blog2.getDocId());
            assertThat(bh.getDocId(), is(blog2.getDocId()));
            assertThat(bh.getPublishedContentId(), is(contentId));
            assertThat(bh.getContentVers().size(), is(2));

            List<Integer> cIds = bh.getContentVers().stream().map(c -> c.getContentId()).collect(Collectors.toList());
            assertThat(cIds, hasItems(contentId, contentId2));

            // Unpublish
            blogService.unpublish(blog2.getDocId());
            blog2 = blogService.get(blog.getDocId());
            assertThat(blog2.getPublishedContent(), nullValue());
        } finally {
            blogService.delete(blog.getDocId());
        }
    }

    @Test
    public void getNextOrPrevBlog() throws Exception {
        // Create
        List<Doc> blogs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Doc blog = DataUtils.createDoc(Doc.Type.BLOG, Content.Format.ADOC,
                    "admin", "findList test", "BlogServiceTest *test*");

            blogService.create(blog);
            blogs.add(blog);

            if (i % 2 == 0) {
                blog.setPublishedUser("admin");
                blog.setPublishedDt(LocalDateTime.now().plus(1, ChronoUnit.MILLIS)); // Set published with gap on purpose for testing.
                blogService.publish(blog);
            }

            // Ensure create in order to test find Next/Prev
            Thread.sleep(300);
        }

        try {
            // Get Latest List
            List<Doc> list = blogService.findLatest(new Paging()).getList();
            list = list.stream().filter(b -> b.getLatestContent().getTitle().equals("findList test")).collect(Collectors.toList());
            assertThat(list.size(), is(10));
            list.forEach(doc -> assertThat(doc.getType(), is(Doc.Type.BLOG)));

            // Get Published List
            list = blogService.findPublished(new Paging()).getList();
            list = list.stream().filter(b -> b.getLatestContent().getTitle().equals("findList test")).collect(Collectors.toList());
            assertThat(list.size(), is(5));
            list.forEach(doc -> assertThat(doc.getType(), is(Doc.Type.BLOG)));

            // Find Next - next older blog
            Doc blog2;
            blog2 = blogService.getNextBlog(blogs.get(8).getDocId(), blogs.get(8).getPublishedDt());
            assertThat(blog2.getDocId(), is(blogs.get(6).getDocId()));
            blog2 = blogService.getNextBlog(blogs.get(6).getDocId(), blogs.get(6).getPublishedDt());
            assertThat(blog2.getDocId(), is(blogs.get(4).getDocId()));
            blog2 = blogService.getNextBlog(blogs.get(0).getDocId(), blogs.get(0).getPublishedDt());
            if (blog2 != null) {
                assertThat(blog2.getLatestContent().getTitle(), not("findList test"));
            }

            // Find Previous
            blog2 = blogService.getPrevBlog(blogs.get(0).getDocId(), blogs.get(0).getPublishedDt());
            assertThat(blog2.getDocId(), is(blogs.get(2).getDocId()));
            blog2 = blogService.getPrevBlog(blogs.get(2).getDocId(), blogs.get(2).getPublishedDt());
            assertThat(blog2.getDocId(), is(blogs.get(4).getDocId()));
            blog2 = blogService.getPrevBlog(blogs.get(8).getDocId(), blogs.get(8).getPublishedDt());
            if (blog2 != null) {
                assertThat(blog2.getLatestContent().getTitle(), not("findList test"));
            }
        } finally {
            for (Doc blog : blogs) {
                blogService.delete(blog.getDocId());
            }
        }
    }
}