<!DOCTYPE html>
<html>
<head>
<#include "/themes/${app.themeName}/includes/html-head.ftl">
</head>
<body>
<#include "/themes/${app.themeName}/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <p>This is a blogger application that helps you publish and manage blog posts.
            You can also customize the pages and update the site layout, just like a CMS application.</p>

        <p>If you face any issues using this application, please submit a ticket
        <a href="https://github.com/zemian/adocblog/issues">here</a>.</p>

        <#include "/includes/disqus.ftl">
    </div>

    <#include "/themes/${app.themeName}/includes/footer.ftl">
</div>

<#include "/themes/${app.themeName}/includes/html-tail.ftl">
</body>
</html>
