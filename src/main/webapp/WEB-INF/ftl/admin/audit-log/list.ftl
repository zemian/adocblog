<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">
<div class="container">
    <div class="app-content">
        <h1>AuditLog List View</h1>

        <#if message??>
            <p class="alert alert-success">${message}</p>
        </#if>

        <table class="table">
            <tr>
                <td>ID</td>
                <td>Name</td>
                <td>Value</td>
                <td>Created Dt</td>
                <td>Actions</td>
            </tr>
            <#list plist.list as auditLog>
                <tr>
                    <td>${auditLog.logId!''}</td>
                    <td>${auditLog.name!''}</td>
                    <td>${auditLog.value!''[0..30]}</td>
                    <td>${auditLog.createdDt!''}</td>
                    <td>
                        <a href="${app.contextPath}/admin/audit-log/detail/${auditLog.logId}"><span title="Detail" class="glyphicon glyphicon-zoom-in" aria-hidden="true"></span> View</a>
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
