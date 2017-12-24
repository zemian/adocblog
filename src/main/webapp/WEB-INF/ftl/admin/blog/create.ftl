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
        <h1>New Blog Post</h1>

        <#if actionErrorMessage??>
            <p class="alert alert-danger">${actionErrorMessage}</p>
        </#if>

        <form class="form-horizontal" action="${app.contextPath}/admin/blog/create" method="post">
            <div class="form-group">
                <label for="title">Title:</label>
                <input type="input" class="form-control" id="title" name="title" value="${title!''}">
            </div>
            <div class="form-group">
                <label for="format">Format:</label>
                <select class="form-control" id="format" name="format">
                    <option <#if (format!'ADOC') == 'ADOC'>selected="true"</#if> value="ADOC">ADOC</option>
                    <option <#if (format!'ADOC') == 'HTML'>selected="true"</#if> value="HTML">HTML</option>
                </select>
            </div>
            <div class="form-group">
                <label for="title">Tags:</label>
                <input type="input" class="form-control" id="tags" name="tags" value="${tags!''}">
            </div>
            <div class="form-group">
                <textarea id="contentText" name="contentText" rows="30" cols="100">${contentText!''}</textarea>
            </div>
            <button class="btn btn-success" name="btnAction" value="save">Save</button>
            <button class="btn btn-success" name="btnAction" value="publish">Publish</button>
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