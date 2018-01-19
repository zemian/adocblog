<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Update Password for User: ${user.username}</h1>

        <form id="blog" class="form-horizontal" method="post" action="${app.contextPath}/admin/user/password">
            <input type="hidden" id="username" name="username" value="${user.username}">

            <div class="form-group">
                <label for="password" class="col-sm-4 control-label">New Password </label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" id="password" name="password" value="">
                </div>
                <div class="col-sm-4"></div>
            </div>

            <div class="form-group">
                <div class="col-sm-4"></div>
                <div class="col-sm-4"><button type="submit" class="btn btn-default">Submit</button></div>
                <div class="col-sm-4"></div>
            </div>
        </form>
    </div>
<#include "/admin/includes/footer.ftl">
</div>

<#include "/admin/includes/html-tail.ftl">
</body>
</html>
