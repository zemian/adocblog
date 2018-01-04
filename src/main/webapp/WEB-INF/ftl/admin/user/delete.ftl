<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Delete User</h1>

        <p>Are you sure you want to delete Setting with ID ${user.username}?</p>

        <form id="setting" class="form-horizontal" method="post" action="${app.contextPath}/admin/user/delete">
            <input type="hidden" id="username" name="username" value="${user.username}">
            <div class="form-group">
                <div class="col-sm-12">
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
