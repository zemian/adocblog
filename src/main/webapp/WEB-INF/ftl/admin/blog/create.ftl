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
        <h1>New Blog Post</h1>

        <#if actionErrorMessage??>
            <p class="alert alert-danger">${actionErrorMessage}</p>
        </#if>

        <form class="form-horizontal" action="${app.contextPath}/admin/blog/create" method="post">
            <div class="form-group">
                <label for="title">Subject:</label>
                <input type="input" class="form-control" id="title" name="title">
            </div>
            <div class="form-group">
                <label for="format">Format:</label>
                <input type="input" class="form-control" id="format" name="format" value="ADOC">
            </div>
            <div class="form-group">
                <textarea id="contentText" name="contentText" rows="30" cols="100"></textarea>
            </div>
            <button class="btn btn-success">Submit</button>
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