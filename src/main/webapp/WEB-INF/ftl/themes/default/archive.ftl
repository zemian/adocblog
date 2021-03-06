<!DOCTYPE html>
<html>
<head>
<#include "/themes/${app.themeName}/includes/html-head-noasciidoc.ftl">
</head>
<body>
<#include "/themes/${app.themeName}/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Blog Archives</h1>
        <#if tags?size gt 0><p>Search by tags:
            <#list tags as tag><a href="${app.contextPath}/archive/tags/${tag}">${tag}</a> </#list>
        </p></#if>
        <#if years?size gt 0><p>Search by years:
            <#list years as year><a href="${app.contextPath}/archive/${year}">${year}</a> </#list>
        </p></#if>
        <table class="table">
            <thead>
                <th>Title</th>
                <th>Date</th>
                <th>Author</th>
            </thead>
            <#list blogs.list as blog>
                <tr>
                    <td><a href="${app.contextPath}/blog/${blog.docId}">${blog.latestContent.title}</a></td>
                    <td>${blog.publishedDt.format(app.config['app.web.blogDateFormat'])}</td>
                    <td>${blog.publishedContent.authorFullName}</td>
                </tr>
            </#list>
        </table>

        <!-- Pagination -->
        <ul class="pager">
            <#if blogs.paging.offset gt 0 && blogs.prevPageOffset gte 0><li><a href="?offset=${blogs.prevPageOffset}">Previous Page</a></li></#if>
            <#if blogs.more><li><a href="?offset=${blogs.nextPageOffset}">Next Page</a></li></#if>
        </ul>
    </div>
<#include "/themes/${app.themeName}/includes/footer.ftl">
</div>

<#include "/themes/${app.themeName}/includes/html-tail.ftl">
</body>
</html>

