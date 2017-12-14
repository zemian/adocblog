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

        <form class="form-horizontal" action="${app.contextPath}/admin/blog/edit" method="post">
            <div class="form-group">
                <label for="title">Subject:</label>
                <input type="input" class="form-control" id="title" name="title" value="${blog.latestContent.title}">
            </div>
            <div class="form-group">
                <label for="format">Format:</label>
                <input type="input" class="form-control" id="format" name="format" value="${blog.contentMeta.format}">
            </div>
            <div class="form-group">
                <label for="reasonForEdit">Reason For Edit:</label>
                <input type="input" class="form-control" id="reasonForEdit" name="reasonForEdit">
            </div>
            <div class="form-group">
                <textarea id="contentText" name="contentText" rows="30" cols="100">${blogContentText}</textarea>
            </div>
            <button class="btn btn-success">Submit</button>
            <input type="hidden" name="blogId" value="${blog.blogId}">
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