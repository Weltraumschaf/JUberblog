package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.freemarkerdown.VariableName;

/**
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public enum TemplateVariables implements VariableName {
    CONTENT("content"),
    DESCRIPTION("description"),
    ENCODING("encoding"),
    ITEMS("items"),
    LANGUAGE("language"),
    LAST_BUILD_DATE("lastBuildDate"),
    LINK("link"),
    POSTS("posts"),
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
