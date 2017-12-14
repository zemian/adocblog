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
    <div class="main-app">
        <h1>Edit Blog Post</h1>

        <#if actionErrorMessage??>
            <p class="alert alert-danger">${actionErrorMessage}</p>
        </#if>

        <form class="form-horizontal" action="${app.contextPath}/admin/page/edit" method="post">
            <div class="form-group">
                <label for="pathName">Path Name:</label>
                <input type="input" class="form-control" id="pathName" name="pathName" value="${page.pathName}">
            </div>
            <div class="form-group">
                <label for="format">Format:</label>
                <input type="input" class="form-control" id="format" name="format" value="${page.contentMeta.format}">
            </div>
            <div class="form-group">
                <textarea id="contentText" name="contentText" rows="30" cols="100">${pageContentText}</textarea>
            </div>
            <button class="btn btn-success">Submit</button>
            <input type="hidden" name="pageId" value="${page.pageId}">
        </form>
        <script>
            CodeMirror.fromTextArea(document.getElementById("contentText"), {
                lineNumbers: true,
                lineWrapping: true
            });
        </script>
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>