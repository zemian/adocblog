<!DOCTYPE html>
<html>
<head>
<#include "/themes/${app.themeName}/includes/html-head.ftl">
</head>
<body>
<#include "/themes/${app.themeName}/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <#if blog??>
            <h1><a href="${app.contextPath}/blog/${blog.docId}">${blog.publishedContent.title}</a></h1>
            <p class="author">${blog.publishedDt.format(app.config['app.web.blogDateFormat'])} by
                ${blog.publishedContent.authorFullName}
                <#if app.config['app.web.disqus.websiteName']?has_content>
                <a href="${app.contextPath}/blog/${blog.docId}#disqus_thread">Comment</a>
                </#if>
                <#if blog.tags??>
                        <p class="tags">
                            Tags:
                        <#list blog.tags?split(" ") as tag><a href="${app.contextPath}/archive/tags/${tag}">${tag}</a> </#list>
                        </p>
                </#if>
            </p>
            <div class="blog-post">
            ${blog.publishedContent.contentText}
            </div>
        <#else>
            <p>Welcome! Go to <a href="${app.contextPath}/admin">Admin Console</a> to create your first post!</p>
        </#if>

        <#if blogs.list?size gt 0>
            <h2>Older Posts</h2>
            <ul>
                <#list blogs.list as blog>
                    <li>
                        <a href="${app.contextPath}/blog/${blog.docId}">${blog.publishedContent.title}</a>
                        ${blog.publishedDt.format(app.config['app.web.blogDateFormat'])} by ${blog.publishedContent.authorFullName}
                    </li>
                </#list>
                <#if blogs.more>
                    <p><a href="${app.contextPath}/archive">... more</a></p>
                </#if>
            </ul>
        </#if>
    </div>
    <#include "/themes/${app.themeName}/includes/footer.ftl">
</div>

<#include "/themes/${app.themeName}/includes/html-tail.ftl">
</body>
</html>
