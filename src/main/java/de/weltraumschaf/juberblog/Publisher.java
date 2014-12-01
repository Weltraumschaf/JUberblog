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

import de.weltraumschaf.commons.validate.Validate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Publisher {

    private final FilesFinder finder = new FilesFinder(FileNameExtension.MARKDOWN);
    private final Path inputDir;
    private final Path outputDir;
    private final String encoding;
    private final Renderer renderer;

    public Publisher(final Path inputDir, final Path outputDir, final Path layoutTemplateFile, final Path postTemplateFile, final String encoding) throws IOException {
        super();
        this.inputDir = Validate.notNull(inputDir, "inputDir");
        this.outputDir = Validate.notNull(outputDir, "outputDir");
        this.renderer = new Renderer(
                Validate.notNull(layoutTemplateFile, "layoutTemplateFile"),
                Validate.notNull(postTemplateFile, "postTemplateFile"),
                encoding
        );
        this.encoding = Validate.notEmpty(encoding, "encoding");
    }

    public void publish() throws IOException {
        for (final DataFile foundPostData : finder.find(inputDir)) {
            Files.write(
                    outputDir.resolve(foundPostData.getBareName() + FileNameExtension.HTML.getExtension()),
                    renderer.render(foundPostData.getPath()).getBytes(encoding)
            );
        }
    }
}
