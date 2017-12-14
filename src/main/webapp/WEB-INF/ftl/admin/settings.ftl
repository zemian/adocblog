<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="main-app">
        <h1>Settings Table</h1>
        <table class="table">
            <#list settings.list as setting>
                <tr>
                    <td>${setting.settingId}</td>
                    <td>${setting.category}</td>
                    <td>${setting.name}</td>
                    <td>${setting.value}</td>
                </tr>
            </#list>
        </table>
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
