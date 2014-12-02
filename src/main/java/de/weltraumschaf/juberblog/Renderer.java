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
package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.Layout;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.freemarkerdown.PreProcessor;
import de.weltraumschaf.freemarkerdown.PreProcessors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

/**
 * Orchestrates the whole two step template and preprocessing stuff to render a post or site.
 *
 * <p>
 * Structure of template fragemnts:
 * </p>
 * <pre>
 * +-------------------+
 * |   outerTemplate   |
 * | +---------------+ |
 * | | innerTemplate | |
 * | |  +---------+  | |
 * | |  | content |  | |
 * | |  +---------+  | |
 * | +---------------+ |
 * +-------------------+
 * </pre>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class Renderer {

    /**
     * Encoding used to transfer byte[] and string.
     */
    private final String encoding;
    /**
     * Used to render the templates.
     *
     * XXX: Inject one instance from main app.
     */
    private final FreeMarkerDown fmd = FreeMarkerDown.create();
    /**
     * Outer part of the two step layout.
     */
    private final Layout outerTemplate;
    /**
     * Inner part of the two step layout.
     */
    private final Layout innerTemplate;

    /**
     * Dedicated constructor.
     *
     * @param outerTemplate must not be {@code null}
     * @param innerTemplate must not be {@code null}
     * @param encoding must not be {@code null} or empty
     * @throws IOException if templates can't be read
     */
    public Renderer(final Path outerTemplate, final Path innerTemplate, final String encoding) throws IOException {
        super();
        this.encoding = Validate.notEmpty(encoding, "encoding");
        this.outerTemplate = fmd.createLayout(outerTemplate, encoding, RenderOptions.WITHOUT_MARKDOWN);
        this.innerTemplate = fmd.createLayout(innerTemplate, encoding, RenderOptions.WITHOUT_MARKDOWN);
        this.outerTemplate.assignTemplateModel("content", this.innerTemplate);
    }

    /**
     * Renders the given content fragment at given file location.
     * <p>
     * Throws {@link IllegalArgumentException} if given path does not exist or is a directory.
     * </p>
     *
     * @param content must not be {@code null}
     * @return
     * @throws IOException
     */
    RendererResult render(final Path content) throws IOException {
        Validate.notNull(content, "content");

        if (!Files.exists(content)) {
            throw new IllegalArgumentException(String.format("Given path '%s' does not exist!", content));
        }

        if (Files.isDirectory(content)) {
            throw new IllegalArgumentException(String.format("Given path '%s' is a directory!", content));
        }

        innerTemplate.assignTemplateModel("content", fmd.createFragemnt(content, encoding));
        outerTemplate.assignVariable("name", "NAME");
        outerTemplate.assignVariable("description", "DESCRIPTION");

        final Map<String, String> keyValues = Maps.newHashMap();
        final PreProcessor processor = PreProcessors.createKeyValueProcessor(keyValues);
        fmd.register(processor);

        return new RendererResult(fmd.render(outerTemplate), keyValues);
    }

    /**
     * Provides the rendering result.
     * <p>
     * This object is necessary to return multiple values at one time.
     * </p>
     */
    static final class RendererResult {
        /**
         * The rendered content (usually HTML).
         */
        private final String renderedContent;
        /**
         * Meta data found by pre processors of FreeMarkerDown.
         */
        private final Map<String, String> metaData;

        /**
         * Dedicated constructor.
         *
         * @param renderedContent must not be {@code null}
         * @param metaData must not be {@code null}
         */
        private RendererResult(final String renderedContent, final Map<String, String> metaData) {
            super();
            this.renderedContent = Validate.notNull(renderedContent, "renderedContent");
            this.metaData = Validate.notNull(metaData, "metaData");
        }

        /**
         * Get the rendered content.
         *
         * @return never {@code null}
         */
        String getRenderedContent() {
            return renderedContent;
        }

        /**
         * Get found meta data.
         *
         * @return never {@code null}, unmodifiable
         */
        Map<String, String> getMetaData() {
            return Collections.unmodifiableMap(metaData);
        }

    }
}
