<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">
<div class="container">
    <div class="app-content">
        <h1>AuditLog Detail</h1>

        <p>
            <a href="${app.contextPath}/admin/audit-log/list"><span title="List" class="glyphicon glyphicon-list" aria-hidden="true"></span> List</a>
        </p>

        <table class="table">
            <tr>
                <td>ID</td><td>${auditLog.logId}</td>
            </tr>
            <tr>
                <td>Name</td><td>${auditLog.name!''}</td>
            </tr>
            <tr>
                <td>Value</td><td>${auditLog.value!''}</td>
            </tr>
            <tr>
                <td>Created Dt</td><td>${auditLog.createdDt!''}</td>
            </tr>
        </table>
    </div>
<#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
