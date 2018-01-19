<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">
<div class="container">
    <div class="app-content">
        <h1>User List View</h1>

        <#if message??>
            <p class="alert alert-success">${message}</p>
        </#if>


        <p>
            <a href="${app.contextPath}/admin/user/create">
                <span title="Create" class="glyphicon glyphicon-plus" aria-hidden="true"></span> Create
            </a>
        </p>

        <table class="bordered-table">
            <thead>
                <th>Username</th>
                <th>Full Name</th>
                <th>Admin</th>
                <th>CreatedDt</th>
                <th>Actions</th>
            </thead>
            <#list plist.list as user>
                <tr>
                    <td>${user.username!''}</td>
                    <td>${user.fullName!''}</td>
                    <td>${user.admin!''}</td>
                    <td>${user.createdDt!''}</td>
                    <td>
                        <a href="${app.contextPath}/admin/user/detail/${user.username}"><span title="Detail" class="glyphicon glyphicon-zoom-in" aria-hidden="true"></span> View</a>
                        <a href="${app.contextPath}/admin/user/edit/${user.username}"><span title="Edit" class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</a>
                        <a href="${app.contextPath}/admin/user/delete/${user.username}"><span title="Delete" class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</a>
                        <#--
                        <a href="${app.contextPath}/admin/user/password/${user.username}"><span title="Update Password" aria-hidden="true"></span> Update Password</a>
                        -->
                    </td>
                </tr>
            </#list>
        </table>

        <!-- Pagination -->
        <ul class="pager">
        <#if plist.paging.offset gt 0 && plist.prevPageOffset gte 0><li><a href="?offset=${plist.prevPageOffset}">Previous Page</a></li></#if>
        <#if plist.more><li><a href="?offset=${plist.nextPageOffset}">Next Page</a></li></#if>
        </ul>
    </div>
<#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
