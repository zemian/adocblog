<!DOCTYPE html>
<html>
<head>
<#include "/themes/${app.themeName}/includes/html-head.ftl">
</head>
<body>
<#include "/themes/${app.themeName}/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <#if errorMessage??>
            <p class="alert alert-danger">${errorMessage}</p>
        <#else>
            <h1>${blog.publishedContent.title}</h1>
            <p class="author">${blog.publishedDt.format(app.config['app.web.blogDateFormat'])} by
                ${blog.publishedContent.authorFullName}
                <#if app.config['app.web.disqus.websiteName']?has_content>
                    <script id="dsq-count-scr" src="//${app.config['app.web.disqus.websiteName']}.disqus.com/count.js" async></script>
                    <a href="#disqus_thread">Comment</a>
                </#if>
                <#if blog.tags??><p class="tags">Tags: ${blog.tags}</p></#if>
            </p>
            <div class="blog-post">
                ${blog.publishedContent.contentText}
            </div>

            <!-- Next/Previous Post -->
            <p>
            <#if prevBlog??>
                <a href="${app.contextPath}/blog/${prevBlog.docId}">Previous Blog</a>
                <#if nextBlog??>|</#if>
            </#if>
            <#if nextBlog??>
                <a href="${app.contextPath}/blog/${nextBlog.docId}">Next Blog</a>
            </#if>
            </p>
        </#if>

        <#if !(errorMessage??)>
            <#include "/includes/disqus.ftl">
        </#if>
    </div>
    <#include "/themes/${app.themeName}/includes/footer.ftl">
</div>

<#include "/themes/${app.themeName}/includes/html-tail.ftl">
</body>
</html>
