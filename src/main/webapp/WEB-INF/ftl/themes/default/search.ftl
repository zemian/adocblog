<!DOCTYPE html>
<html>
<head>
<#include "/themes/${app.themeName}/includes/html-head.ftl">
</head>
<body>
<#include "/themes/${app.themeName}/includes/header.ftl">

<div class="container">
    <div class="main-content">
        <h1>Search Result</h1>
        <table class="table">
            <tr>
                <td>Subject</td>
                <td>Date</td>
                <td>Author</td>
            </tr>
            <#list blogs.list as blog>
                <tr>
                    <td><a href="${app.contextPath}/blog/${blog.blogId}">${blog.subject}</a></td>
                    <td>${blog.publication.publishDt.format(blogDateFormat)}</td>
                    <td>${blog.contentMeta.author}</td>
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

