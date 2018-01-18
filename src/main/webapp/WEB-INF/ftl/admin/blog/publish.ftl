<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Publish Blog</h1>

        <p>Are you sure you want to publish Blog ID ${doc.docId} with ContentID ${contentId}?</p>

        <form id="blog" class="form-horizontal" method="post" action="${app.contextPath}/admin/blog/publish">
            <input type="hidden" id="docId" name="docId" value="${doc.docId}">
            <input type="hidden" id="contentId" name="contentId" value="${contentId}">

            <div class="form-group">
                <label for="publishDate" class="col-sm-4 control-label">Publish Date (YYYY-MM-DD) </label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" id="publishDate" name="publishDate" value="${publishDate.format('yyyy-MM-dd')}">
                </div>
                <div class="col-sm-4"></div>
            </div>

            <div class="form-group">
                <div class="col-sm-4"></div>
                <div class="col-sm-4"><button type="submit" class="btn btn-default">Submit</button></div>
                <div class="col-sm-4"></div>
            </div>
        </form>
    </div>
<#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
