<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">
<div class="container">
    <div class="app-content">
        <h1>Edit User</h1>

        <#include "/admin/includes/form-errors.ftl">

        <form id="user" class="form-horizontal" method="post" action="${app.contextPath}/admin/user/edit">
            <input type="hidden" id="username" name="username" value="${user.username}">
            <div class="form-group">
                <label for="id" class="col-sm-2 control-label">Username</label>
                <div id="id" class="col-sm-10">
                    <p class="form-control-static">${user.username}</p>
                </div>
            </div>
            <#include "/admin/user/form-body.ftl">
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
