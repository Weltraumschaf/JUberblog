<?xml version="1.0" encoding="${encoding}"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
<#list urls as url>
    <url>
        <loc>${url.loc}</loc>
        <lastmod>${url.lastmod}</lastmod>
        <changefreq>${url.changefreq}</changefreq>
        <priority>${url.priority}</priority>
    </url>
</#list>
</urlset>