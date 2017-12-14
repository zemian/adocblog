<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
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
            <#list blogHistory.contentVers as content>

                <#assign pubActionLabel = 'Publish' >
                <#assign pubActionPath = 'publish/${blogHistory.blogId}/${content.contentId}' >
                <#if content.contentId == blogHistory.publishedContentId!(0)>
                    <#assign pubActionLabel = 'Unpublish' >
                    <#assign pubActionPath = 'unpublish/${blogHistory.blogId}' >
                </#if>

                <tr>
                    <td>${content.createdDt}</td>
                    <td>${content.version}</td>
                    <td>${content.reasonForEdit!''}</td>
                    <td>${content.createdUser}</td>
                    <td>${content.format}</td>
                    <td>${(content.contentId == blogHistory.publishedContentId!(0))?string('Yes', 'No')}</td>
                    <td>
                        <a href="${app.contextPath}/admin/blog/preview/${blogHistory.blogId}/${content.contentId}">Preview</a> |
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