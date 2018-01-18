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

        <form id="blog" class="form-inline" method="post" action="${app.contextPath}/admin/blog/publish">
            <input type="hidden" id="docId" name="docId" value="${doc.docId}">
            <input type="hidden" id="contentId" name="contentId" value="${contentId}">
            <div class="form-group">
                <div class="col-sm-12">
                    <button type="submit" class="btn btn-default" name="pubNow">Publish Now</button>
                    OR
                    <button type="submit" class="btn btn-default" name="publishWithDate">Publish with Specific Date: </button>
                    <input class="form-control" type="text" id="publishDate" name="publishDate" placeholder="YYYY-mm-dd HH:MM">
                </div>
            </div>
        </form>
    </div>
<#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
