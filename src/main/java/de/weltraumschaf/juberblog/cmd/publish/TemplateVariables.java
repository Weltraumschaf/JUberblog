package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.freemarkerdown.VariableName;

/**
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public enum TemplateVariables implements VariableName {
    BASE_URL("baseUrl"),
    BLOG_DESCRIPTION("blogDescription"),
    BLOG_TITLE("blogTitle"),
    BLOG_VERSION("blogVersion"),
    CHANGEFREQ("changefreq"),
    DC_DATE("dcDate"),
    CONTENT("content"),
    DESCRIPTION("description"),
    ENCODING("encoding"),
    ITEMS("items"),
    KEYWORDS("keywords"),
    LANGUAGE("language"),
    LAST_BUILD_DATE("lastBuildDate"),
    LASTMOD("lastmod"),
    LINK("link"),
    LOC("loc"),
    NAVIGATION("navi"),
    POSTS("posts"),
    PRIORITY("priority"),
    PUB_DATE("pubDate"),
    TITLE("title"),
    URLS("urls");

    private final String variableName;

    private TemplateVariables(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String getVariableName() {
        return variableName;
    }

}
