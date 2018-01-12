<!DOCTYPE html>
<html>
<head>
<!--
We do not include user theme html-head here because that will bring asciidoc styling, which for blog display only.
For archive and search, we do not want those style.
-->
<#include "/includes/html-head.ftl">
</head>
<body>
<#include "/themes/${app.themeName}/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Search Result</h1>
        <table class="table">
            <tr>
                <th>Title</th>
                <th>Date</th>
                <th>Author</th>
            </tr>
            <#list blogs.list as blog>
                <tr>
                    <td><a href="${app.contextPath}/blog/${blog.docId}">${blog.latestContent.title}</a></td>
                    <td>${blog.publishedDt.format(app.config['app.web.blogDateFormat'])}</td>
                    <td>${blog.publishedContent.authorFullName}</td>
                </tr>
            </#list>
        </table>

        <!-- Pagination -->
        <p>
        <#if blogs.paging.offset gt 0 && blogs.prevPageOffset gte 0><a href="?offset=${blogs.prevPageOffset}&searchTerms=${searchTerms}">Previous Page</a> <#if blogs.more>|</#if></#if>
        <#if blogs.more><a href="?offset=${blogs.nextPageOffset}&searchTerms=${searchTerms}">Next Page</a></#if>
        </p>
    </div>
<#include "/themes/${app.themeName}/includes/footer.ftl">
</div>

<#include "/themes/${app.themeName}/includes/html-tail.ftl">
</body>
</html>

