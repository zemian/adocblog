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
        <h1>New Blog Content</h1>

        <#include "/admin/includes/form-errors.ftl">

        <form class="form-horizontal" action="${app.contextPath}/admin/blog/create" method="post">
            <#include "/admin/page/form-body.ftl">
        </form>
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>