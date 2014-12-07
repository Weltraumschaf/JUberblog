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

import com.beust.jcommander.internal.Lists;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.commons.validate.Validate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Publish pages from given data files into a given directory.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Publisher {

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
    private final String baseUrlForPosts;
    /**
     * Renders the page (Markdown/templates).
     */
    private final Renderer renderer;

    /**
     * Dedicated constructor.
     *
     * @param inputDir must not be {@code null}
     * @param outputDir must not be {@code null}
     * @param layoutTemplateFile must not be {@code null}
     * @param postTemplateFile must not be {@code null}
     * @param encoding must not be {@code null} or empty
     * @throws IOException
     */
    public Publisher(final Path inputDir, final Path outputDir, final Path layoutTemplateFile, final Path postTemplateFile, final String encoding, final String baseUrlForPosts) throws IOException {
        super();
        this.inputDir = Validate.notNull(inputDir, "inputDir");
        this.outputDir = Validate.notNull(outputDir, "outputDir");
        this.renderer = new Renderer(
                Validate.notNull(layoutTemplateFile, "layoutTemplateFile"),
                Validate.notNull(postTemplateFile, "postTemplateFile"),
                encoding
        );
        this.encoding = Validate.notEmpty(encoding, "encoding");
        this.baseUrlForPosts = Validate.notEmpty(baseUrlForPosts, "baseUrlForPosts");
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
    public Collection<Page> publish() throws IOException {
        final Collection<Page> publishedPages = Lists.newArrayList();

        for (final DataFile foundPostData : FilesFinderByExtension.MARKDOWN.find(inputDir)) {
            final Renderer.RendererResult result = renderer.render(foundPostData.getPath());
            final String outputBaseName = foundPostData.getBareName() + FileNameExtension.HTML.getExtension();

            Files.write(outputDir.resolve(outputBaseName), result.getRenderedContent().getBytes(encoding));

            // XXX: Check if present, extract etc.
            final Map<String, String> metaData = result.getMetaData();
            final String description = metaData.containsKey("Description")
                    ? metaData.get("Description")
                    : ""; // TODO Extract excerpt from Markdown.

            // XXX: Emit errors if something of this is not available.
            publishedPages.add(new Page(
                     headline.find(result.getMarkdown()),
                    baseUrlForPosts + "/" + outputBaseName,
                    description,
                    foundPostData.getCreationDate()));
        }

        return Collections.unmodifiableCollection(publishedPages);
    }

}
