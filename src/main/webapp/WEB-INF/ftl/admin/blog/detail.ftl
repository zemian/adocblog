<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">
<div class="container">
    <div class="app-content">
        <h1>Blog Detail</h1>

        <p>
            <a href="${app.contextPath}/admin/blog/list"><span title="List" class="glyphicon glyphicon-list" aria-hidden="true"></span> List</a>
            <a href="${app.contextPath}/admin/blog/edit/${doc.docId}"><span title="Edit" class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</a>
            <a href="${app.contextPath}/admin/blog/delete/${doc.docId}"><span title="Delete" class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</a>
            <a href="${app.contextPath}/admin/blog/history/${doc.docId}">History</a>
            <a href="${app.contextPath}/admin/blog/preview/${doc.docId}/${doc.latestContent.contentId}" target="_blank"><span title="Preview"></span> Preview</a>
        </p>

        <table class="table">
            <tr>
                <td>ID</td><td>${doc.docId}</td>
            </tr>
            <tr>
                <td>Title</td><td>${doc.latestContent.title!''}</td>
            </tr>
            <tr>
                <td>Tags</td><td>${doc.tags!''}</td>
            </tr>
            <tr>
                <td>Latest Content Date</td><td>${doc.latestContent.createdDt!''}</td>
            </tr>
            <tr>
                <td>Latest Version</td><td>${doc.latestContent.version!''}</td>
            </tr>
            <tr>
                <td>Published Content Date</td><td>${doc.publishedDt!'NOT PUBLISHED'}</td>
            </tr>
            <tr>
                <td>Published Version</td><td><#if doc.publishedDt??>${doc.publishedContent.version}<#else>NOT PUBLISHED</#if></td>
            </tr>
        </table>
    </div>
<#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
