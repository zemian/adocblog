<!DOCTYPE html>
<html>
<head>
<#include "/themes/${app.themeName}/includes/html-head.ftl">
</head>
<body>
<#include "/themes/${app.themeName}/includes/header.ftl">

<div class="container">
    <div class="app-content">

        <p></p>

        <p>This is Java based blogger application that supports easy to use ASCIIDOC format.</p>

        <p>If you have any issues, please submit a ticket
        <a href="https://github.com/zemian/adocblog/issues">here</a>.</p>

        <#include "/includes/disqus.ftl">
    </div>

    <#include "/themes/${app.themeName}/includes/footer.ftl">
</div>

<#include "/themes/${app.themeName}/includes/html-tail.ftl">
</body>
</html>
