<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="main-content">
        <h1>System Information</h1>
        <table class="table">
            <#list sysInfo as key, value>
                <tr><td>${key}</td><td>${value}</td></tr>
            </#list>
        </table>
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
