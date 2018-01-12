<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Welcome to Admin Console</h1>
        <p>There are ${publishedBlogsCount?string.number} published blogs
            and ${(totalBlogsCount - publishedBlogsCount)?string.number} unpublished blogs.</p>
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
