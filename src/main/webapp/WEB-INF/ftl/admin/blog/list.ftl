<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Blog List View</h1>

        <p>
            <a href="${app.contextPath}/admin/blog/create">
                <span title="Create" class="glyphicon glyphicon-plus" aria-hidden="true"></span> Create
            </a>
        </p>

        <#if actionSuccessMessage??>
            <p class="alert alert-success">${actionSuccessMessage}</p>
        <#elseif actionErrorMessage??>
            <p class="alert alert-danger">${actionErrorMessage}</p>
        </#if>

        <table class="bordered-table">
            <tr>
                <th rowspan="2">ID</th>
                <th colspan="4">Title</th>
                <th rowspan="2">Actions</th>
            </tr>
            <tr>
                <th>Latest Date</th>
                <th>Latest Ver</th>
                <th>Published Date</th>
                <th>Published Ver</th>
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
                    <td rowspan="2">${doc.docId}</td>
                    <td colspan="4">${doc.latestContent.title}</td>
                    <td rowspan="2">
                        <a href="${app.contextPath}/admin/blog/preview/${doc.docId}/${doc.latestContent.contentId}" target="_blank"><span title="Detail" class="glyphicon glyphicon-zoom-in" aria-hidden="true"></span> Preview</a>
                        <a href="${app.contextPath}/admin/blog/edit/${doc.docId}"><span title="Edit" class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</a>
                        <a href="${app.contextPath}/admin/blog/delete/${doc.docId}"><span title="Delete" class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</a>
                        <a href="${app.contextPath}/admin/blog/history/${doc.docId}">History</a>
                        <a href="${app.contextPath}/admin/blog/${pubActionPath}" data-toggle="confirmation" data-title="Are you sure?">${pubActionLabel}</a>
                    </td>
                </tr>
                <tr>
                    <td>${doc.latestContent.createdDt}</td>
                    <td>${doc.latestContent.version}</td>
                    <td>${pubDate}</td>
                    <td>${pubVersion}</td>
                </tr>
            </#list>
        </table>

        <!-- Pagination -->
        <ul class="pager">
            <#if docs.paging.offset gt 0 && docs.prevPageOffset gte 0><li><a href="?offset=${docs.prevPageOffset}">Previous Page</a></li></#if>
            <#if docs.more><li><a href="?offset=${docs.nextPageOffset}">Next Page</a></li></#if>
        </ul>
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
