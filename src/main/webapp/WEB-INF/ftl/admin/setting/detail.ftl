<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">
<div class="container">
    <div class="app-content">
        <h1>Setting Detail</h1>

        <p>
            <a href="${app.contextPath}/admin/setting/list"><span title="List" class="glyphicon glyphicon-list" aria-hidden="true"></span> List</a>
            <a href="${app.contextPath}/admin/setting/edit/${setting.settingId}"><span title="Edit" class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</a>
            <a href="${app.contextPath}/admin/setting/delete/${setting.settingId}"><span title="Delete" class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</a>
        </p>

        <table class="table">
            <tr>
                <td>ID</td><td>${setting.settingId}</td>
            </tr>
            <tr>
                <td>Category</td><td>${setting.category!''}</td>
            </tr>
            <tr>
                <td>Name</td><td>${setting.name!''}</td>
            </tr>
            <tr>
                <td>Value</td><td>${setting.value!''}</td>
            </tr>
            <tr>
                <td>Type</td><td>${setting.type!''}</td>
            </tr>
            <tr>
                <td>Description</td><td>${setting.description!''}</td>
            </tr>
        </table>
    </div>
<#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
