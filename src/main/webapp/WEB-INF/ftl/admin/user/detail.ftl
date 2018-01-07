<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">
<div class="container">
    <div class="app-content">
        <h1>User Detail</h1>

        <p>
            <a href="${app.contextPath}/admin/user/list"><span title="List" class="glyphicon glyphicon-list" aria-hidden="true"></span> List</a>
            <a href="${app.contextPath}/admin/user/edit/${user.username}"><span title="Edit" class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</a>
            <a href="${app.contextPath}/admin/user/delete/${user.username}"><span title="Delete" class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</a>
        </p>

        <table class="table">
            <tr>
                <td>Username</td><td>${user.username}</td>
            </tr>
            <tr>
                <td>Full Name</td><td>${user.fullName!''}</td>
            </tr>
            <tr>
                <td>Admin</td><td>${user.admin!''}</td>
            </tr>
            <tr>
                <td>Deleted</td><td>${user.deleted!''}</td>
            </tr>
            <tr>
                <td>CreatedDt</td><td>${user.createdDt!''}</td>
            </tr>
        </table>
    </div>
<#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
