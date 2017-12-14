<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
<link rel="stylesheet" href="${app.contextPath}/resources/css/admin-preview.css" />
</head>
<body class="preview-background">
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="main-app">
        <h1>${blog.subject}</h1>
        ${blogContentText}
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>