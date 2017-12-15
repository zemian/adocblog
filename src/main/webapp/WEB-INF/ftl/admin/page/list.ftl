<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Pages Management</h1>

        <p><a href="${app.contextPath}/admin/page/create">Create New Page</a></p>

        <#if actionSuccessMessage??>
            <p class="alert alert-success">${actionSuccessMessage}</p>
        <#elseif actionErrorMessage??>
            <p class="alert alert-danger">${actionErrorMessage}</p>
        </#if>

        <table class="table">
            <tr>
                <td>ID</td>
                <td>Path</td>
                <td>Title</td>
                <td>Date</td>
                <td>LatestVer</td>
                <td>PublishedVer</td>
                <td>Actions</td>
            </tr>
            <#list pages.list as page>

                <#assign pubActionLabel = 'Publish' >
                <#assign pubActionPath = 'publish/${page.docId}/${page.latestContent.contentId}' >
                <#assign pubVersion = 'NOT PUBLISHED' >
                <#if page.publishedContent??>
                    <#assign pubActionLabel = 'Unpublish' >
                    <#assign pubActionPath = 'unpublish/${page.docId}' >
                    <#assign pubVersion = page.publishedContent.version >
                </#if>
                <tr>
                    <td>${page.docId}</td>
                    <td>${page.path}</td>
                    <td>${page.latestContent.title}</td>
                    <td>${page.latestContent.createdDt}</td>
                    <td>${page.latestContent.version}</td>
                    <td>${pubVersion}</td>
                    <td>
                        <a href="${app.contextPath}/admin/page/edit/${page.docId}">Edit</a> |
                        <a href="${app.contextPath}/admin/page/preview/${page.docId}/${page.latestContent.contentId}" target="_blank">Preview</a> |
                        <a href="${app.contextPath}/admin/page/history/${page.docId}">History</a> |
                        <a href="${app.contextPath}/admin/page/delete/${page.docId}" data-toggle="confirmation" data-title="Are you sure?">Delete</a> |
                        <a href="${app.contextPath}/admin/page/${pubActionPath}" data-toggle="confirmation" data-title="Are you sure?">${pubActionLabel}</a>
                    </td>
                </tr>
            </#list>
        </table>

        <!-- Pagination -->
        <p>
        <#if pages.paging.offset gt 0 && pages.prevPageOffset gte 0><a href="?offset=${pages.prevPageOffset}">Previous Page</a> <#if pages.more>|</#if></#if>
        <#if pages.more><a href="?offset=${pages.nextPageOffset}">Next Page</a></#if>
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
