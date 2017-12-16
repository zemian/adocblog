<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Settings Table</h1>
        <table class="table">
            <td>ID</td>
            <td>Category</td>
            <td>Name</td>
            <td>Value</td>
            <#list settings.list as setting>
                <tr>
                    <td>${setting.settingId}</td>
                    <td>${setting.category}</td>
                    <td>${setting.name}</td>
                    <td>${setting.value}</td>
                </tr>
            </#list>
        </table>

        <!-- Pagination -->
        <p>
        <#if settings.paging.offset gt 0 && settings.prevPageOffset gte 0><a href="?offset=${settings.prevPageOffset}">Previous Page</a> <#if settings.more>|</#if></#if>
        <#if settings.more><a href="?offset=${settings.nextPageOffset}">Next Page</a></#if>
        </p>
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
