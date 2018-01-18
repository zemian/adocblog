<!DOCTYPE html>
<html>
<head>
<#include "/admin/includes/html-head.ftl">
</head>
<body>
<#include "/admin/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <h1>Delete Page</h1>

        <p>Are you sure you want to delete Page with ID ${doc.docId}: ${doc.latestContent.title}?</p>

        <form id="page" class="form-horizontal" method="post" action="${app.contextPath}/admin/page/delete">
            <input type="hidden" id="docId" name="docId" value="${doc.docId}">
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
