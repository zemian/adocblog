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
        <h1>New Page Post</h1>

        <#if actionErrorMessage??>
            <p class="alert alert-danger">${actionErrorMessage}</p>
        </#if>

        <form class="form-horizontal" action="${app.contextPath}/admin/page/create" method="post">
            <div class="form-group">
                <label for="title">Title:</label>
                <input type="input" class="form-control" id="title" name="title">
            </div>
            <div class="form-group">
                <label for="path">Path:</label>
                <input type="input" class="form-control" id="path" name="path">
            </div>
            <div class="form-group">
                <label for="format">Format:</label>
                <select class="form-control" id="format" name="format">
                    <option selected="true" value="FTL">FTL</option>
                    <option value="ADOC">ADOC</option>
                    <option value="HTML">HTML</option>
                    <option value="TEXT">TEXT</option>
                    <option value="JSON">JSON</option>
                </select>
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