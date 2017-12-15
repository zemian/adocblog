<!DOCTYPE html>
<html>
<head>
<#include "/includes/html-head.ftl">
</head>
<body>
<#include "/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Oops! Something went wrong.</h1>
        <p>${Request['javax.servlet.error.status_code']} ${Request['javax.servlet.error.request_uri']}</p>
        <div class="alert alert-danger" role="alert">
            <p>${errorMessage!''}</p>
        </div>
        <!--
        <h3>Exception</h3>
        <pre>${exceptionStacktrace!''}</pre>
        -->
    </div>
<#include "/includes/footer.ftl">
</div>

<#include "/includes/html-tail.ftl">
</body>
</html>
