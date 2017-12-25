<#if app.config['app.web.googleAnalytic.id']?has_content>
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=${app.config['app.web.googleAnalytic.id']}"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', "${app.config['app.web.googleAnalytic.id']}");
    </script>
</#if>