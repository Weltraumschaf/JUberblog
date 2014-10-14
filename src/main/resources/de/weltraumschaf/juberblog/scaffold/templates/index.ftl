<h2>All Blog Posts</h2>
<ul>
    <#list posts as post>
        <li>
            <a href="${post.url}">${post.title}</a>
            <span class="meta">(${post.publishingDate})</span>
        </li>
    </#list>
</ul>