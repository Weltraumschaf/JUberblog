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
package de.weltraumschaf.juberblog.publish;

import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.Page.Pages;
import de.weltraumschaf.juberblog.core.Page.SortByDateAscending;
import de.weltraumschaf.juberblog.core.Page.Type;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

/**
 * Publish pages from given data files into a given directory.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Publisher {

    /**
     * USed to extract headline.
     */
    private final Headline headline = new Headline();
    /**
     * Where to find data files.
     */
    private final Path inputDir;
    /**
     * Where to store published pages as file.
     */
    private final Path outputDir;
    /**
     * Used encoding for IO.
     */
    private final String encoding;
    /**
     * Base URL of published pages.
     */
    private final URI baseUrlForPages;
    /**
     * Renders the page (Markdown/templates).
     */
    private final Renderer renderer;
    /**
     * Type of published pages.
     */
    private final Type type;

    /**
     * Dedicated constructor.
     *
     * XXX: Use builder to reduce constructor parameters.
     *
     * @param inputDir must not be {@code null}
     * @param outputDir must not be {@code null}
     * @param layoutTemplateFile must not be {@code null}
     * @param postTemplateFile must not be {@code null}
     * @param encoding must not be {@code null} or empty
     * @param baseUrlForPages must not be {@code null}
     * @param type must not be {@code null}
     * @throws IOException if templates can't be read
     */
    public Publisher(
            final Path inputDir,
            final Path outputDir,
            final Path layoutTemplateFile,
            final Path postTemplateFile,
            final String encoding,
            final URI baseUrlForPages,
            final Type type) throws IOException {
        super();
        this.inputDir = Validate.notNull(inputDir, "inputDir");
        this.outputDir = Validate.notNull(outputDir, "outputDir");
        this.renderer = new Renderer(
                Validate.notNull(layoutTemplateFile, "layoutTemplateFile"),
                Validate.notNull(postTemplateFile, "postTemplateFile"),
                encoding
        );
        this.encoding = Validate.notEmpty(encoding, "encoding");
        this.baseUrlForPages = Validate.notNull(baseUrlForPages, "baseUrlForPages").normalize();
        this.type = Validate.notNull(type, "type");
    }

    /**
     * Runs the publishing.
     * <p>
     * The returned collection contains a meta data object for each published page.
     * </p>
     *
     * @return never {@code null}, unmodifiable
     * @throws IOException on any file IO error
     */
    public Pages publish() throws IOException {
        final Pages publishedPages = new Pages();

        for (final DataFile foundData : FilesFinderByExtension.MARKDOWN.find(inputDir)) {
            final Renderer.RendererResult result = renderer.render(foundData.getPath());
            final String outputBaseName = foundData.getBareName() + FileNameExtension.HTML.getExtension();

            Files.write(outputDir.resolve(outputBaseName), result.getRenderedContent().getBytes(encoding));

            // XXX: Check if present, extract etc.
            final Map<String, String> metaData = result.getMetaData();
            final String description = metaData.containsKey("Description")
                    ? metaData.get("Description")
                    : ""; // TODO Extract excerpt from Markdown.

            final URI baseUrl;

            switch (type) {
                case SITE:
                    baseUrl = baseUrlForPages.resolve("/sites/").normalize();
                    break;
                case POST:
                    baseUrl = baseUrlForPages.resolve("/posts/").normalize();
                    break;
                default:
                    throw new IllegalStateException(String.format("Unsupported page type '%s'!", type));
            }
            // XXX: Emit errors if something of this is not available.
            publishedPages.add(new Page(
                    headline.find(result.getMarkdown()),
                    baseUrl.resolve(outputBaseName).normalize(),
                    description,
                    foundData.getCreationDate(),
                    type));
        }

        Collections.sort(publishedPages, new SortByDateAscending());
        return publishedPages;
    }

}
