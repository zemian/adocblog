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
        <h1>Edit Page Post</h1>

        <#if actionErrorMessage??>
            <p class="alert alert-danger">${actionErrorMessage}</p>
        </#if>

        <form class="form-horizontal" action="${app.contextPath}/admin/page/edit" method="post">
            <div class="form-group">
                <label for="title">Title:</label>
                <input type="input" class="form-control" id="title" name="title" value="${page.latestContent.title}">
            </div>
            <div class="form-group">
                <label for="format">Format:</label>
                <div class="form-group">
                    <label for="format">Format:</label>
                    <select class="form-control" id="format" name="format">
                        <option selected="true" value="${page.latestContent.format}">${page.latestContent.format}</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="path">Path:</label>
                <input type="input" class="form-control" id="path" name="path" value="${page.path}">
            </div>
            <div class="form-group">
                <label for="reasonForEdit">Reason For Edit:</label>
                <input type="input" class="form-control" id="reasonForEdit" name="reasonForEdit">
            </div>
            <div class="form-group">
                <textarea id="contentText" name="contentText" rows="30" cols="100">${pageContentText}</textarea>
            </div>
            <button class="btn btn-success">Submit</button>
            <input type="hidden" name="pageId" value="${page.docId}">
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