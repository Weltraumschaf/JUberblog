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

import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.filter.MarkdownFilter;
import de.weltraumschaf.juberblog.template.Layout;
import de.weltraumschaf.juberblog.template.Template;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

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
abstract class BaseFormatter implements Formatter {

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
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@code null}
     * @param contentTemplate must not be {@code null} or empty
     * @throws IOException if template file can't be read
     */
    public BaseFormatter(final Configuration templateConfiguration, final String contentTemplate) throws IOException {
        super();
        Validate.notNull(templateConfiguration, "Template configuration must not be null!");
        Validate.notEmpty(contentTemplate, "Layout template must not be null or empty!");
        content = new Layout(templateConfiguration, contentTemplate);
        layout = new Layout(templateConfiguration, LAYOUT_TEMPLATE);
        layout.setContent(content);
    }

    @Override
    public String format(final InputStream markdownFile) throws IOException, TemplateException {
        final Template input = new Template(IOUtils.toString(markdownFile), Constants.DEFAULT_ENCODING.toString());
        input.addPostFilter(new MarkdownFilter());
        content.setContent(input);
        return layout.render();
    }

}
