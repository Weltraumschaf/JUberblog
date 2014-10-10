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
package de.weltraumschaf.juberblog.formatter;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.filter.MarkdownFilter;
import de.weltraumschaf.juberblog.template.Layout;
import de.weltraumschaf.juberblog.template.Template;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;

/**
 * Implements the base of all formatters.
 *
 * All formatters are a stack of:
 * <pre>
 * +---------------+
 * |    layout     |
 * | +-----------+ |
 * | |  content  | |
 * | | +-------+ | |
 * | | | input | | |
 * | | +-------+ | |
 * | +-----------+ |
 * +---------------+
 * </pre>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
abstract class BaseFormatter implements HtmlFormatter {

    /**
     * Name of layout template.
     */
    private static final String LAYOUT_TEMPLATE = "layout.ftl";
    /**
     * Inner two step template.
     */
    protected final Layout content;
    /**
     * Outer two step template.
     */
    private final Layout layout;
    /**
     * Markdown to format.
     */
    private final String markdown;

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@literal null}
     * @param contentTemplate must not be {@literal null} or empty
     * @param markdown  must not be {@literal null}
     * @throws IOException if template file can't be read
     */
    public BaseFormatter(final Configuration templateConfiguration, final String contentTemplate, final String markdown)
        throws IOException {
        super();
        Validate.notNull(templateConfiguration, "Template configuration must not be null!");
        Validate.notEmpty(contentTemplate, "Layout template must not be null or empty!");
        Validate.notNull(markdown, "Markdown string must not be null!");
        content = new Layout(templateConfiguration, contentTemplate);
        layout = new Layout(templateConfiguration, LAYOUT_TEMPLATE);
        layout.setContent(content);
        this.markdown = markdown;
    }

    @Override
    public String format() throws IOException, TemplateException {
        final Template input = new Template(markdown, Constants.DEFAULT_ENCODING.toString());
        input.addPostFilter(new MarkdownFilter());
        content.setContent(input);
        return layout.render();
    }

    @Override
    public void setTitle(final String title) {
        layout.setTitle(title);
    }

    @Override
    public void setEncoding(final String encoding) {
        layout.setEncoding(encoding);
    }

    @Override
    public void setBaseUri(final String baseUri) {
        layout.setBaseUri(baseUri);
    }

    @Override
    public void setDescription(final String description) {
        layout.setDescription(description);
    }

    @Override
    public void setKeywords(final String keywords) {
        layout.setKeywords(keywords);
    }

    @Override
    public void setVersion(final String version) {
        layout.setVersion(version);
    }


    @Override
    public void setHeadline(final String headline) {
        layout.setHeadline(headline);
    }
}
