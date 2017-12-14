<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="main-app">
        <h1>Blog Versions History</h1>
        <table class="table">
            <tr>
                <td>Date</td>
                <td>Version</td>
                <td>Reason</td>
                <td>Author</td>
                <td>Format</td>
                <td>Published</td>
                <td>Actions</td>
            </tr>
            <#list blogs.list as blog>

                <#assign pubActionLabel = 'Publish' >
                <#assign pubActionPath = 'publish/${blog.blogId}/${blog.contentMeta.contentId}' >
                <#if blog.publication??>
                    <#assign pubActionLabel = 'Unpublish' >
                    <#assign pubActionPath = 'unpublish/${blog.blogId}' >
                </#if>

                <tr>
                    <td>${blog.contentMeta.createdDt}</td>
                    <td>${blog.contentMeta.contentVersion}</td>
                    <td>${blog.contentMeta.contentVersionReason!''}</td>
                    <td>${blog.contentMeta.createdUser}</td>
                    <td>${blog.contentMeta.format}</td>
                    <td>${(blog.publication??)?string('Yes', 'No')}</td>
                    <td>
                        <a href="${app.contextPath}/admin/blog/preview/${blog.blogId}/${blog.contentMeta.contentId}">Preview</a> |
                        <a href="${app.contextPath}/admin/blog/${pubActionPath}" data-toggle="confirmation" data-title="Are you sure?">${pubActionLabel}</a>
                    </td>
                </tr>
            </#list>
        </table>
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