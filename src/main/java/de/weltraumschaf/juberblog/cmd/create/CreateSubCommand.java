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
package de.weltraumschaf.juberblog.cmd.create;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.string.StringEscape;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.cmd.CommonCreateAndPublishSubCommand;
import de.weltraumschaf.juberblog.opt.CreateOptions;
import de.weltraumschaf.juberblog.template.Template;
import de.weltraumschaf.juberblog.time.TimeProvider;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.log4j.Logger;

/**
 * Creates a post or site optionally as draft.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class CreateSubCommand extends CommonCreateAndPublishSubCommand<CreateOptions> {

    /**
     * Template for XML.
     */
    private static final String TEMPLATE = "post_or_site.md.ftl";
    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(CreateSubCommand.class);

    /**
     * Dedicated constructor.
     *
     * @param io must not be {@code null}
     */
    public CreateSubCommand(final IO io) {
        super(io);
    }

    @Override
    public void run() {
        final String title = getOptions().getTitle().trim();

        if (title.isEmpty()) {
            io.errorln("Empty title not allowed!");
            return; // TODO Throw application exception with exit code.
        }

        try {
            final Template tpl = new Template(getTemplateConfig(), TEMPLATE);
            tpl.assignVariable("title", title);
            final String content = tpl.render();

            if (getOptions().isSite()) {
                createSite(content);
            } else {
                createPost(content);
            }
        } catch (final IOException ex) {
            io.errorln("Can't read template to create post/site!");
            return; // TODO Throw application exception with exit code.
        } catch (final TemplateException ex) {
            io.errorln("Can't render template to create post/site!");
            return; // TODO Throw application exception with exit code.
        }

        io.println("Done :)");
    }

    private void createSite(final String content) {
        final String title = getOptions().getTitle();
        final Path baseDir;

        if (getOptions().isDraft()) {
            io.println(String.format("Create site draft '%s'...", title));
            baseDir = getDirectories().dataDraftSites();
        } else {
            io.println(String.format("Create site '%s'...", title));
            baseDir = getDirectories().dataSites();
        }

        createFile(createFileName(baseDir, title), content);
    }

    private void createPost(final String content) {
        final String title = getOptions().getTitle();
        final Path baseDir;

        if (getOptions().isDraft()) {
            io.println(String.format("Create post draft '%s'...", title));
            baseDir = getDirectories().dataDraftPosts();
        } else {
            io.println(String.format("Create post '%s'...", title));
            baseDir = getDirectories().dataPosts();
        }

        createFile(createFileName(baseDir, title), content);
    }

    private void createFile(final Path fileName, final String content) {
        io.println(String.format("Write file '%s'...", fileName));
    }

    private Path createFileName(final Path Path, final String title) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    String createFileNameFromTitle(final String title, final TimeProvider time) {
        final StringBuilder buffer = new StringBuilder();

        buffer.append(Validate.notNull(time).nowAsString())
              .append('_')
              .append(StringEscape.escapeFileName(Validate.notEmpty(title)))
              .append(".md");

        return buffer.toString();
    }
}
