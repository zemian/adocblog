<nav class="navbar navbar-expand-md navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="${app.contextPath}/">${app.config['app.web.name']}</a>
        </div>
        <div id="navbarCollapse" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li id="blog"><a href="${app.contextPath}/admin/blog/list">Blogs</a></li>
                <#if userSession.user.admin>
                    <li id="page"><a href="${app.contextPath}/admin/page/list">Pages</a></li>
                    <li id="user"><a href="${app.contextPath}/admin/user/list">Users</a></li>
                    <li id="setting"><a href="${app.contextPath}/admin/setting/list">Settings</a></li>
                    <li id="audit-log"><a href="${app.contextPath}/admin/audit-log/list">AuditLogs</a></li>
                    <li id="system-info"><a href="${app.contextPath}/admin/system-info">SystemInfo</a></li>
                </#if>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <p class="navbar-text"><strong>User: ${userSession.user.username}</strong></p>
                <li><a href="${app.contextPath}/logout">Logout</a></li>
            </ul>
        </div>
    </div>
</nav>