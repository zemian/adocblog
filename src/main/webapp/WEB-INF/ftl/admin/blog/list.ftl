<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="main-app">
        <h1>Blogs Management</h1>

        <p><a href="${app.contextPath}/admin/blog/create">Create New Post</a></p>

        <#if actionSuccessMessage??>
            <p class="alert alert-success">${actionSuccessMessage}</p>
        <#elseif actionErrorMessage??>
            <p class="alert alert-danger">${actionErrorMessage}</p>
        </#if>

        <table class="table">
            <tr>
                <td>ID</td>
                <td>Subject</td>
                <td>Date</td>
                <td>LatestVer</td>
                <td>PublishedVer</td>
                <td>Actions</td>
            </tr>
            <#list blogs.list as blog>

                <#assign pubActionLabel = 'Publish' >
                <#assign pubActionPath = 'publish/${blog.blogId}/${blog.contentMeta.contentId}' >
                <#assign pubVersion = 'NOT PUBLISHED' >
                <#if blog.publication??>
                    <#assign pubActionLabel = 'Unpublish' >
                    <#assign pubActionPath = 'unpublish/${blog.blogId}' >
                    <#assign pubVersion = blog.publication.version >
                </#if>
                <tr>
                    <td>${blog.blogId}</td>
                    <td>${blog.subject}</td>
                    <td>${blog.contentMeta.createdDt}</td>
                    <td>${blog.contentMeta.contentVersion}</td>
                    <td>${pubVersion}</td>
                    <td>
                        <a href="${app.contextPath}/admin/blog/edit/${blog.blogId}">Edit</a> |
                        <a href="${app.contextPath}/admin/blog/preview/${blog.blogId}/${blog.contentMeta.contentId}" target="_blank">Preview</a> |
                        <a href="${app.contextPath}/admin/blog/history/${blog.blogId}">History</a> |
                        <a href="${app.contextPath}/admin/blog/delete/${blog.blogId}" data-toggle="confirmation" data-title="Are you sure?">Delete</a> |
                        <a href="${app.contextPath}/admin/blog/${pubActionPath}" data-toggle="confirmation" data-title="Are you sure?">${pubActionLabel}</a>
                    </td>
                </tr>
            </#list>
        </table>

        <!-- Pagination -->
        <p>
        <#if blogs.paging.offset gt 0 && blogs.prevPageOffset gte 0><a href="?offset=${blogs.prevPageOffset}">Previous Page</a> <#if blogs.more>|</#if></#if>
        <#if blogs.more><a href="?offset=${blogs.nextPageOffset}">Next Page</a></#if>
        </p>
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
<script src="${app.contextPath}/resources/js/bootstrap-confirmation.min.js"></script>
<script>
    $('[data-toggle=confirmation]').confirmation();
</script>

</body>
</html>
