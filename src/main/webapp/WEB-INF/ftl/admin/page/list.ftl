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
                <td>LatestDate</td>
                <td>LatestVer</td>
                <td>PublishedDate</td>
                <td>PublishedVer</td>
                <td>Actions</td>
            </tr>
            <#list docs.list as doc>

                <#assign pubActionLabel = 'Publish' >
                <#assign pubActionPath = 'publish/${doc.docId}/${doc.latestContent.contentId}' >
                <#assign pubVersion = 'NOT PUBLISHED' >
                <#assign pubDate = 'NOT PUBLISHED' >
                <#if doc.publishedContent??>
                    <#assign pubActionLabel = 'Unpublish' >
                    <#assign pubActionPath = 'unpublish/${doc.docId}' >
                    <#assign pubDate = doc.publishedDt >
                    <#assign pubVersion = doc.publishedContent.version >
                </#if>
                <tr>
                    <td>${doc.docId}</td>
                    <td>${doc.path}</td>
                    <td>${doc.latestContent.title}</td>
                    <td>${doc.latestContent.createdDt}</td>
                    <td>${doc.latestContent.version}</td>
                    <td>${pubDate}</td>
                    <td>${pubVersion}</td>
                    <td>
                        <a href="${app.contextPath}/admin/page/edit/${doc.docId}">Edit</a> |
                        <a href="${app.contextPath}/admin/page/preview/${doc.docId}/${doc.latestContent.contentId}" target="_blank">Preview</a> |
                        <a href="${app.contextPath}/admin/page/history/${doc.docId}">History</a> |
                        <a href="${app.contextPath}/admin/page/delete/${doc.docId}" data-toggle="confirmation" data-title="Are you sure?">Delete</a> |
                        <a href="${app.contextPath}/admin/page/${pubActionPath}" data-toggle="confirmation" data-title="Are you sure?">${pubActionLabel}</a>
                    </td>
                </tr>
            </#list>
        </table>

        <!-- Pagination -->
        <p>
        <#if docs.paging.offset gt 0 && docs.prevPageOffset gte 0><a href="?offset=${docs.prevPageOffset}">Previous Page</a> <#if docs.more>|</#if></#if>
        <#if docs.more><a href="?offset=${docs.nextPageOffset}">Next Page</a></#if>
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
