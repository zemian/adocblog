<!DOCTYPE html>
<html>
<head>
<#include "/themes/${app.themeName}/includes/html-head.ftl">
</head>
<body>
<#include "/themes/${app.themeName}/includes/header.ftl">

<div class="container">
    <div class="app-content">
        <#if errorMessage??>
            <p class="alert alert-danger">${errorMessage}</p>
        <#else>
            <h1>${blog.publishedContent.title}</h1>
            <p>${blog.publishedDt.format(app.config['app.web.blogDateFormat'])} by
                ${blog.publishedContent.authorFullName}
                <#if app.config['app.web.disqus.websiteName']?has_content>
                <script id="dsq-count-scr" src="//${app.config['app.web.disqus.websiteName']}.disqus.com/count.js" async></script>
                <a href="#disqus_thread">Link</a>
                </#if>
            </p>
            <div class="blog-post">
                ${blog.publishedContent.contentText}
            </div>

            <!-- Next/Previous Post -->
            <p>
            <#if prevBlog??>
                <a href="${app.contextPath}/blog/${prevBlog.docId}">Previous Blog</a>
                <#if nextBlog??>|</#if>
            </#if>
            <#if nextBlog??>
                <a href="${app.contextPath}/blog/${nextBlog.docId}">Next Blog</a>
            </#if>
            </p>
        </#if>
    </div>
    <#include "/themes/${app.themeName}/includes/footer.ftl">

    <#if app.config['app.web.disqus.websiteName']?has_content>
        <div id="disqus_thread"></div>
        <script>

            /**
             *  RECOMMENDED CONFIGURATION VARIABLES: EDIT AND UNCOMMENT THE SECTION BELOW TO INSERT DYNAMIC VALUES FROM YOUR PLATFORM OR CMS.
             *  LEARN WHY DEFINING THESE VARIABLES IS IMPORTANT: https://disqus.com/admin/universalcode/#configuration-variables*/
            /*
            var disqus_config = function () {
            this.page.url = PAGE_URL;  // Replace PAGE_URL with your page's canonical URL variable
            this.page.identifier = PAGE_IDENTIFIER; // Replace PAGE_IDENTIFIER with your page's unique identifier variable
            };
            */
            (function() { // DON'T EDIT BELOW THIS LINE
                var d = document, s = d.createElement('script');
                s.src = 'https://${app.config['app.web.disqus.websiteName']}.disqus.com/embed.js';
                s.setAttribute('data-timestamp', +new Date());
                (d.head || d.body).appendChild(s);
            })();
        </script>
        <noscript>Please enable JavaScript to view the <a href="https://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
    </#if>
</div>

<#include "/themes/${app.themeName}/includes/html-tail.ftl">
</body>
</html>
