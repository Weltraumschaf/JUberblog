<?xml version="1.0" encoding="${encoding}"?>
<rss xmlns:content="http://purl.org/rss/1.0/modules/content/"
     xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd"
     xmlns:dc="http://purl.org/dc/elements/1.1/"
     version="2.0"
     xmlns:trackback="http://madskills.com/public/xml/rss/module/trackback/">
    <channel>
        <title>${feed.title}</title>
        <link>${feed.link}</link>
        <description>${feed.description}</description>
        <language>${feed.language}</language>
        <lastBuildDate>${feed.lastBuildDate}</lastBuildDate>
        <#list feed.items as item>
        <item>
            <title>${item.title}</title>
            <link>${item.link}</link>
            <description>${item.description}</description>
            <pubDate>${item.pubDate}</pubDate>
            <dc:date>${item.dcDate}</dc:date>
        </item>
        </#list>
    </channel>
</rss>