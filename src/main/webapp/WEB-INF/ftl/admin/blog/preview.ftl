<!DOCTYPE html>
<html>
<head>
<#include "/includes/asciidoctor-head.ftl">
<#include "/admin/includes/html-head.ftl">
<link rel="stylesheet" href="${app.contextPath}/resources/css/admin-preview.css" />
</head>
<body class="preview-background">
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>${doc.latestContent.title}</h1>
        ${contentText}
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>