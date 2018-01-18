<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Page List View</h1>

        <p>
            <a href="${app.contextPath}/admin/page/create">
                <span title="Create" class="glyphicon glyphicon-plus" aria-hidden="true"></span> Create
            </a>
        </p>

        <#if actionSuccessMessage??>
            <p class="alert alert-success">${actionSuccessMessage}</p>
        <#elseif actionErrorMessage??>
            <p class="alert alert-danger">${actionErrorMessage}</p>
        </#if>

        <table class="table">
            <thead>
                <th>ID</th>
                <th>Path</th>
                <th>Version</th>
                <th>Date</th>
                <th>Actions</th>
            </thead>
            <#list docs.list as doc>
                <#assign pubActionLabel = 'Publish' >
                <#assign pubActionPath = 'publish/${doc.docId}/${doc.latestContent.contentId}' >
                <#assign docVersion = doc.latestContent.version >
                <#assign docDate = doc.latestContent.createdDt >
                <#if doc.publishedContent??>
                    <#assign pubActionLabel = 'Unpublish' >
                    <#assign pubActionPath = 'unpublish/${doc.docId}' >
                    <#assign docDate = doc.publishedDt >
                    <#assign docVersion = doc.publishedContent.version >
                </#if>
                <tr>
                    <td>${doc.docId}</td>
                    <td>${doc.path}</td>
                    <td>${docVersion}</td>
                    <td>${docDate.format('yyyy-MM-dd')}</td>
                    <td>
                        <a href="${app.contextPath}/admin/page/detail/${doc.docId}"><span title="Detail" class="glyphicon glyphicon-zoom-in" aria-hidden="true"></span> View</a>
                        <a href="${app.contextPath}/admin/page/edit/${doc.docId}"><span title="Edit" class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</a>
                        <a href="${app.contextPath}/admin/page/delete/${doc.docId}"><span title="Delete" class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</a>
                        <a href="${app.contextPath}/admin/page/${pubActionPath}" data-toggle="confirmation" data-title="Are you sure?">${pubActionLabel}</a>
                        <a href="${app.contextPath}/admin/page/history/${doc.docId}">History</a>
                        <a href="${app.contextPath}/admin/page/preview/${doc.docId}/${doc.latestContent.contentId}" target="_blank"><span title="Preview"></span> Preview</a>
                    </td>
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
