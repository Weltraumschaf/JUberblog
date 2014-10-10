<article>
  <strong class="meta">Posted ${date_formatted}</strong>
<#if features.rating><div id="rating"></div></#if>
  ${content}
</article>

<#if features.comments><div id="comments"></div></#if>

<#if prevPost?has_content || nextPost?has_content>
<nav id="pagination">
  <#if prevPost?has_content>
    <a href="${prevPost}">&laquo; previous post</a>
  </#if>

  <#if prevPost?has_content && nextPost?has_content>
      &ndash;
  </#if>

  <#if nextPost?has_content>
    <a href="${nextPost}">next post &raquo;</a>
  </#if>
</nav>
</#if>

<#if features.comments>
<script id="comments-form-tpl" type="text/x-handlebars-template">
  <h4>Leave a comment</h4>
  <label for="form-comment-name">Name:</label><input id="form-comment-name" type="text"/><br/>
  <label for="form-comment-url">Url:</label><input id="form-comment-url" type="text"/><br/>
  <label for="form-comment-form-comment-">Comment:</label>
  <textarea id="form-comment-text"></textarea>
</script>
<script id="comments-list-tpl" type="text/x-handlebars-template">
  LIST
</script>
</#if>