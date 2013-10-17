<h2>All Blog Posts</h2>
<ul>
    <#list ${posts} as post>
        <li>
            <a href="${post.url}">${post.title}</a>
            <span class="meta">(${post.date_formatted})</span>
        </li>
    </#list>
</ul>