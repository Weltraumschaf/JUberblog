/*
 *  LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 43):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a non alcohol-free beer in return.
 *
 * Copyright (C) 2012 "Sven Strittmatter" <weltraumschaf@googlemail.com>
 */
package de.weltraumschaf.juberblog.template;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import org.apache.commons.lang3.Validate;

/**
 * Extends the {@link Template template} to provide a two step layout.
 *
 * Provides base variables such as title, encoding etc to the layout and inner content template.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Layout extends Template {

    /**
     * Variable key for title.
     */
    private static final String TITLE = "title";
    /**
     * Variable key for content.
     */
    private static final String CONTENT = "content";
    /**
     * Variable key for encoding.
     */
    private static final String ENCODING = "encoding";
    /**
     * Variable key for description.
     */
    private static final String DESCRIPTION = "description";
    /**
     * Inner template to render content string.
     */
    private Renderable content = new DefaultContent();

    /**
     * Initializes all provided template variables with empty strings as default.
     *
     * @param templateConfiguration must not be {@code null}
     * @param layoutTemplateFile must not be {@code null}
     */
    public Layout(final Configuration templateConfiguration, final String layoutTemplateFile) throws IOException {
        super(templateConfiguration, layoutTemplateFile);
        setTitle("");
        setDescription("");
        setEncoding("");
    }

    /**
     * Assign the title to the layout template.
     *
     * @param title must not be {@code null}
     */
    public void setTitle(final String title) {
        assignVariable(TITLE, title);
    }

    /**
     * Assign the description to the layout template.
     *
     * @param description must not be {@code null}
     */
    public void setDescription(final String description) {
        assignVariable(DESCRIPTION, description);
    }

    /**
     * Assign the encoding to the layout template.
     *
     * @param encoding must not be {@code null}
     */
    public void setEncoding(final String encoding) {
        assignVariable(ENCODING, encoding);
    }

    /**
     * Set the inner content template.
     *
     * @param content must not be {@code null}
     */
    public void setContent(final Renderable content) {
        Validate.notNull(content, "Content template must not be null!");
        this.content = content;
    }

    /**
     * Set the content string.
     *
     * @param content must not be {@code null}
     */
    public void setContent(final String content) {
        Validate.notNull(content);
        setContent(new DefaultContent(content));
    }

    @Override
    public String render() throws IOException, TemplateException {
        copyLayoutVariablestoContent();
        assignVariable(CONTENT, content.render());

        return super.render();
    }

    /**
     * Copies all variables assigned to the layout to the inner content template.
     */
    private void copyLayoutVariablestoContent() {
        if (!(content instanceof Template)) {
            return;
        }

        ((Template) content).assignVariable(TITLE, getVariable(TITLE));
        ((Template) content).assignVariable(ENCODING, getVariable(ENCODING));
        ((Template) content).assignVariable(DESCRIPTION, getVariable(DESCRIPTION));
    }

    /**
     * Used as a default template which does only contains a string.
     */
    private static final class DefaultContent implements Renderable {
        /**
         * String which will be used as rendered output.
         */
        private final String content;

        /**
         * Initializes the rendered output with an empty string.
         */
        public DefaultContent() {
            this("");
        }

        /**
         * Dedicated constructor.
         *
         * @param content must not be {@code null}
         */
        public DefaultContent(final String content) {
            super();
            Validate.notNull(content, "Content must not be null!");
            this.content = content;
        }

        @Override
        public String render() throws IOException, TemplateException {
            return content;
        }

    }
}
