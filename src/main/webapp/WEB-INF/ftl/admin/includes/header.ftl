<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="${app.contextPath}/">${app.config['app.web.name']}</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li id="list"><a href="${app.contextPath}/admin/blog/list">Blogs</a></li>
                <li id="list"><a href="${app.contextPath}/admin/page/list">Pages</a></li>
                <#if userSession.user.admin>
                <li id="settings"><a href="${app.contextPath}/admin/settings">Settings</a></li>
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