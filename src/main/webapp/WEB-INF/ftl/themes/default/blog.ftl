<!DOCTYPE html>
<html>
<head>
<#include "/themes/${app.themeName}/includes/html-head.ftl">
</head>
<body>
<#include "/themes/${app.themeName}/includes/header.ftl">

<div class="container">
    <div class="main-content">
        <h1>${blog.subject}</h1>
        <p>${blog.publication.publishDt.format(blogDateFormat)} by ${blog.contentMeta.author}</p>
        <div class="blog-post">
            ${blog.contentText.contentText}
        </div>

        <!-- Next/Previous Post -->
        <p>
        <#if prevBlog??>
            <a href="${app.contextPath}/blog/${prevBlog.blogId}">Previous Blog</a>
            <#if nextBlog??>|</#if>
        </#if>
        <#if nextBlog??>
            <a href="${app.contextPath}/blog/${nextBlog.blogId}">Next Blog</a>
        </#if>
        </p>
    </div>
    <#include "/themes/${app.themeName}/includes/footer.ftl">
</div>

<#include "/themes/${app.themeName}/includes/html-tail.ftl">
</body>
</html>
