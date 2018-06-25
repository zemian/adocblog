<nav class="navbar navbar-expand-md navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="${app.contextPath}/">${app.config['app.web.name']}</a>
        </div>
        <div id="navbarCollapse" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li id="index"><a href="${app.contextPath}/index">Home</a></li>
                <li id="archive"><a href="${app.contextPath}/archive">Archive</a></li>
                <li id="about"><a href="${app.contextPath}/about">About</a></li>
                <li id="about"><a href="${app.contextPath}/api/blog/feed.json"><span title="JSON Feed" class="fa fa-rss" aria-hidden="true"></span></a></li>
            </ul>

            <form class="nav navbar-form navbar-right" method="post" action="${app.contextPath}/search">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Search" name="searchTerms" value="${searchTerms!''}">
                </div>
                <button type="submit" class="btn btn-default">Go</button>
            </form>
        </div>

        <div id="navbar2" class="collapse navbar-collapse">
            <p class="text-md-left app-description">${app.config['app.web.appDescription']}</p>
        </div>
    </div>
</nav>