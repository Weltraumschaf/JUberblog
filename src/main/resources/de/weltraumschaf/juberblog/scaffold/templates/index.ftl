<h3>All Blog Posts</h3>
<ul>
    <#list posts as post>
    <li>
        <a href="${post.link}">${post.title}</a>
        <span>(${post.pubDate})</span>
    </li>
    </#list>
</ul>
