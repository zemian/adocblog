<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
<link rel="stylesheet" href="${app.contextPath}/resources/css/codemirror.css" />
<script src="${app.contextPath}/resources/js/codemirror.js"></script>
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Edit Blog Content</h1>

        <#include "/admin/includes/form-errors.ftl">

        <form class="form-horizontal" action="${app.contextPath}/admin/blog/edit" method="post">
            <input type="hidden" id="docId" name="docId" value="${doc.docId}">
            <div class="form-group">
                <label for="id" class="col-sm-2 control-label">ID</label>
                <div id="id" class="col-sm-10">
                    <p class="form-control-static">${doc.docId}</p>
                </div>
            </div>
            <#include "/admin/page/form-body.ftl">
        </form>
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>