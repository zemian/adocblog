<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">
<div class="container">
    <div class="app-content">
        <h1>Edit Setting</h1>

        <#include "/admin/includes/form-errors.ftl">

        <form id="setting" class="form-horizontal" method="post" action="${app.contextPath}/admin/setting/edit">
            <input type="hidden" id="settingId" name="settingId" value="${setting.settingId}">
            <div class="form-group">
                <label for="id" class="col-sm-2 control-label">ID</label>
                <div id="id" class="col-sm-10">
                    <p class="form-control-static">${setting.settingId}</p>
                </div>
            </div>
            <#include "/admin/setting/form-body.ftl">
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">Submit</button>
                </div>
            </div>
        </form>
    </div>
<#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
