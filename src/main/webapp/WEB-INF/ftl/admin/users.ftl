<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Users</h1>
        <table class="table">
            <tr>
                <td>Username</td>
                <td>Full Name</td>
                <td>Is Admin</td>
            </tr>
            <#list users.list as user>
                <tr>
                    <td>${user.username}</td>
                    <td>${user.fullName}</td>
                    <td>${user.admin?string('YES', 'NO')}</td>
                </tr>
            </#list>
        </table>

        <!-- Pagination -->
        <p>
        <#if users.paging.offset gt 0 && users.prevPageOffset gte 0><a href="?offset=${users.prevPageOffset}">Previous Page</a> <#if users.more>|</#if></#if>
        <#if users.more><a href="?offset=${users.nextPageOffset}">Next Page</a></#if>
        </p>
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
