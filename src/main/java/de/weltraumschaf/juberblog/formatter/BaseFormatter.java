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
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.Layout;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.template.VarName;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    private final FreeMarkerDown fmd;

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@literal null}
     * @param contentTemplate must not be {@literal null} or empty
     * @param markdown  must not be {@literal null}
     * @throws IOException if template file can't be read
     */
    public BaseFormatter(final Configuration templateConfiguration, final String contentTemplate, final String markdown, final Path templateDir)
        throws IOException {
        super();
        Validate.notNull(templateConfiguration, "templateConfiguration");
        fmd = FreeMarkerDown.create(templateConfiguration);
        Validate.notEmpty(contentTemplate, "contentTemplate");
        this.content = fmd.createLayout(templateDir.resolve(contentTemplate));
        this.layout = fmd.createLayout(templateDir.resolve(LAYOUT_TEMPLATE));
        this.layout.assignTemplateModel(VarName.CONTENT.toString(), content);
        this.markdown = Validate.notNull(markdown, "markdown");
    }

    @Override
    public String format() throws IOException, TemplateException {
        final Fragment input = fmd.createFragemnt(markdown, Constants.DEFAULT_ENCODING.toString());
        this.content.assignTemplateModel(VarName.CONTENT.toString(), input);
        return fmd.render(layout);
    }

    @Override
    public void setTitle(final String title) {
        this.layout.assignVariable(VarName.TITLE.toString(), title);
    }

    @Override
    public void setEncoding(final String encoding) {
        this.layout.assignVariable(VarName.ENCODING.toString(), encoding);
    }

    @Override
    public void setBaseUri(final String baseUri) {
        this.layout.assignVariable(VarName.BASE_URI.toString(), baseUri);
    }

    @Override
    public void setDescription(final String description) {
        this.layout.assignVariable(VarName.DESCRIPTION.toString(), description);
    }

    @Override
    public void setKeywords(final String keywords) {
        this.layout.assignVariable(VarName.KEYWORDS.toString(), keywords);
    }

    @Override
    public void setVersion(final String version) {
        this.layout.assignVariable(VarName.VERSION.toString(), version);
    }


    @Override
    public void setHeadline(final String headline) {
        this.layout.assignVariable(VarName.HEADLINE.toString(), headline);
    }
}
