<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Audit Logs</h1>

        <p><a href="${app.contextPath}/admin/audit-logs/remove-old-logs">Remove Old Logs</a></p>

        <#if actionSuccessMessage??>
            <p class="alert alert-success">${actionSuccessMessage}</p>
        </#if>

        <table class="table">
            <td>ID</td>
            <td>Date</td>
            <td>Name</td>
            <td>Value</td>
            <#list auditLogs.list as auditLog>
                <tr>
                    <td>${auditLog.logId}</td>
                    <td>${auditLog.createdDt}</td>
                    <td>${auditLog.name}</td>
                    <td>${auditLog.value}</td>
                </tr>
            </#list>
        </table>

        <!-- Pagination -->
        <p>
        <#if auditLogs.paging.offset gt 0 && auditLogs.prevPageOffset gte 0><a href="?offset=${auditLogs.prevPageOffset}">Previous Page</a> <#if auditLogs.more>|</#if></#if>
        <#if auditLogs.more><a href="?offset=${auditLogs.nextPageOffset}">Next Page</a></#if>
        </p>
    </div>
    <#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
