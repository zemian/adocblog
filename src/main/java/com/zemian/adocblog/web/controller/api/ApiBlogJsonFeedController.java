package com.zemian.adocblog.web.controller.api;

import com.zemian.adocblog.data.dao.Paging;
import com.zemian.adocblog.data.dao.PagingList;
import com.zemian.adocblog.data.domain.Blog;
import com.zemian.adocblog.service.BlogService;
import com.zemian.adocblog.service.ContentService;
import com.zemian.adocblog.web.controller.api.payload.JsonFeed;
import com.zemian.adocblog.web.controller.api.payload.JsonFeedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ApiBlogJsonFeedController {
    @Autowired
    private ContentService contentService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private Environment env;

    @GetMapping(value = "/api/blog/feed.json")
    public @ResponseBody
    JsonFeed latestBlog(Paging paging, HttpServletRequest req) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        String uri = req.getRequestURI();
        String feedUrl = req.getRequestURL().toString();
        String homeUrl = feedUrl.substring(0, feedUrl.indexOf(uri)) + req.getContextPath();

        PagingList<Blog> blogs = blogService.findPublished(paging);
        List<JsonFeedItem> items = new ArrayList<>(blogs.getList().size());
        for (Blog blog : blogs.getList()) {
            String ct = contentService.getContentHtml(blog.getPublishedContent());

            JsonFeedItem item = new JsonFeedItem();
            item.setContentText(ct);
            //item.setAuthor(blog.getAuthor);
            item.setDatePublished(blog.getPublishedDt().atZone(ZoneId.systemDefault()).format(fmt));
            item.setId("" + blog.getBlogId());
            item.setTitle(blog.getPublishedContent().getTitle());
            item.setUrl(homeUrl + "/blog/" + blog.getBlogId());

            item.setVersion(blog.getPublishedContent().getVersion());

            items.add(item);
        }

        JsonFeed ret = new JsonFeed();
        ret.setPaging(blogs.getPaging());
        ret.setMore(blogs.isMore());

        ret.setVersion("https://jsonfeed.org/version/1");
        ret.setTitle(env.getProperty("app.web.title"));
        ret.setDescription(env.getProperty("app.web.blogTitle"));
        ret.setFeedUrl(feedUrl);
        ret.setHomePageUrl(homeUrl);
        ret.setItems(items);

        return ret;
    }
}
