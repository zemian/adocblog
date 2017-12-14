<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="main-app">
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
                <td>PathName</td>
                <td>Date</td>
                <td>Actions</td>
            </tr>
            <#list pages.list as page>
                <tr>
                    <td>${page.pageId}</td>
                    <td>${page.pathName}</td>
                    <td>${page.contentMeta.createdDt}</td>
                    <td>
                        <a href="${app.contextPath}/admin/page/edit/${page.pageId}">Edit</a> |
                        <a href="${app.contextPath}/admin/page/preview/${page.pageId}/${page.contentMeta.contentId}" target="_blank">Preview</a> |
                        <a href="${app.contextPath}/admin/page/delete/${page.pageId}" data-toggle="confirmation" data-title="Are you sure?">Delete</a>
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
