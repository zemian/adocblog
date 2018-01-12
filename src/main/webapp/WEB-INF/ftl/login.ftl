<!DOCTYPE html>
<html>
<head>
<#include "/includes/html-head.ftl">
</head>
<body>
<#include "/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1 class="text-center">Admin Login</h1>
        <div class="row">
            <div class="col-xs-4"></div>
            <div class="col-xs-4">
                <#if loginError??>
                <div class="alert alert-danger" role="alert">
                    <p>${loginError}</p>
                </div>
                </#if>

                <form class="form-horizontal" action="${app.contextPath}/login" method="post">
                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="input" class="form-control" id="username" name="username">
                    </div>
                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" class="form-control" id="password" name="password">
                    </div>
                    <button class="btn btn-success">Submit</button>
                </form>
            </div>
            <div class="col-xs-4"></div>
        </div>
    </div>
<#include "/includes/footer.ftl">
</div>

<#include "/includes/html-tail.ftl">
</body>
</html>
