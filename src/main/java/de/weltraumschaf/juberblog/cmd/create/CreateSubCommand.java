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

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.string.StringEscape;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import de.weltraumschaf.juberblog.cmd.CommonCreateAndPublishSubCommand;
import de.weltraumschaf.juberblog.opt.CreateOptions;
import de.weltraumschaf.juberblog.template.Template;
import de.weltraumschaf.juberblog.time.Time;
import de.weltraumschaf.juberblog.time.TimeProvider;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Creates a post or site optionally as draft.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class CreateSubCommand extends CommonCreateAndPublishSubCommand<CreateOptions> {

    /**
     * Template for XML.
     */
    private static final String TEMPLATE = "create/post_or_site.md.ftl";
    private TimeProvider time = Time.newProvider();

    /**
     * Dedicated constructor.
     *
     * @param io must not be {@code null}
     */
    public CreateSubCommand(final IO io) {
        super(io);
    }

    @Override
    protected void init() throws ApplicationException {
        super.init();
        validateArguments();
    }

    @Override
    public void run() throws ApplicationException {
        final String title = getOptions().getTitle().trim();

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
            throw new ApplicationException(
                ExitCodeImpl.FATAL, "Can't read template to create post/site! (" + ex.getMessage() + ')', ex);
        } catch (final TemplateException ex) {
            throw new ApplicationException(
                ExitCodeImpl.FATAL, "Can't render template to create post/site! (" + ex.getMessage() + ')', ex);
        }

        io.println("Done :)");
    }

    public void setTime(final TimeProvider time) {
        this.time = Validate.notNull(time, "time");
    }

    private void createSite(final String content) throws IOException {
        final String title = getOptions().getTitle();
        final Path baseDir;

        if (getOptions().isDraft()) {
            io.println(String.format("Create site draft '%s'...", title));
            baseDir = getDirectories().dataDraftSites();
        } else {
            io.println(String.format("Create site '%s'...", title));
            baseDir = getDirectories().dataSites();
        }

        writeFile(createPath(baseDir, title), content);
    }

    private void createPost(final String content) throws IOException {
        final String title = getOptions().getTitle();
        final Path baseDir;

        if (getOptions().isDraft()) {
            io.println(String.format("Create post draft '%s'...", title));
            baseDir = getDirectories().dataDraftPosts();
        } else {
            io.println(String.format("Create post '%s'...", title));
            baseDir = getDirectories().dataPosts();
        }

        writeFile(createPath(baseDir, title), content);
    }

    void writeFile(final Path fileName, final String content) throws IOException {
        io.println(String.format("Write file '%s'...", fileName));
        Files.write(fileName, content.getBytes(Constants.DEFAULT_ENCODING.toString()));
    }

    Path createPath(final Path baseDir, final String title) {
        return baseDir.resolve(createFileNameFromTitle(title));
    }

    String createFileNameFromTitle(final String title) {
        final StringBuilder buffer = new StringBuilder();

        buffer.append(time.nowAsString())
              .append('_')
              .append(StringEscape.escapeFileName(Validate.notEmpty(title)))
              .append(".md");

        return buffer.toString();
    }

    private void validateArguments() throws ApplicationException {
        if (getOptions().getTitle().isEmpty()) {
            throw new ApplicationException(ExitCodeImpl.TOO_FEW_ARGUMENTS, "No title arguemnt given!");
        }
    }
}
