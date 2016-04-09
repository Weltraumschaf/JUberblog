<h3>All Blog Posts</h3>
<ul>
    <#list posts as post>
    <li>
        <a href="${post.link}">${post.title}</a>
        <!-- Date format see here http://freemarker.org/docs/ref_builtins_date.html -->
        <span>(${post.pubDate?string["dd.MM.yyyy, HH:mm"]})</span>
    </li>
    </#list>
</ul>
