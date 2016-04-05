package de.weltraumschaf.juberblog.cmd.create;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.string.StringEscape;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.freemarkerdown.TemplateError;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.cmd.SubCommandBase;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.options.CreateOptions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Creates a blog post or site.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class CreateSubCommand extends SubCommandBase {

    /**
     * Used to create the time in the file name.
     */
    private TimeProvider time = Time.newProvider();

    /**
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     */
    public CreateSubCommand(final JUberblog registry) {
        super(registry);
    }

    private CreateOptions createOptions() {
        return options().getCreate();
    }

    @Override
    protected void doExecute() throws ApplicationException {
        validateArguments();
        final String title = createOptions().getTitle().trim();
        final String encoding = configuration().getEncoding();

        try {
            final FreeMarkerDown fmd = FreeMarkerDown.create(encoding);
            final Fragment tpl = fmd.createFragemnt(
                templates().getCreateSiteOrPostTemplate(),
                encoding,
                templates().getCreateSiteOrPostTemplate().toString(),
                RenderOptions.WITHOUT_MARKDOWN);
            tpl.assignVariable("title", title);
            final String content = fmd.render(tpl);

            if (createOptions().isSite()) {
                createSite(content);
            } else {
                createPost(content);
            }
        } catch (final IOException ex) {
            throw new ApplicationException(
                ExitCodeImpl.FATAL, "Can't read template to create post/site! (" + ex.getMessage() + ')', ex);
        } catch (final TemplateError ex) {
            throw new ApplicationException(
                ExitCodeImpl.FATAL, "Can't render template to create post/site! (" + ex.getMessage() + ')', ex);
        }

        io().println("Done :)");
    }

    /**
     * Injection point for a time provider.
     *
     * @param time must not be {@code null}
     */
    void setTime(final TimeProvider time) {
        this.time = Validate.notNull(time, "time");
    }

    /**
     * Create site (draft).
     *
     * @param content must not be {@code null} or empty
     * @throws IOException if file can't be written
     */
    private void createSite(final String content) throws IOException {
        final String title = createOptions().getTitle();
        final Path baseDir;

        if (createOptions().isDraft()) {
            io().println(String.format("Create site draft '%s'...", title));
            baseDir = directories().getSitesDraftData();
        } else {
            io().println(String.format("Create site '%s'...", title));
            baseDir = directories().getSitesData();
        }

        writeFile(createPath(baseDir, title), Validate.notEmpty(content, "content"));
    }

    /**
     * Create post (draft).
     *
     * @param content must not be {@code null} or empty
     * @throws IOException if file can't be written
     */
    private void createPost(final String content) throws IOException {
        final String title = createOptions().getTitle();
        final Path baseDir;

        if (createOptions().isDraft()) {
            io().println(String.format("Create post draft '%s'...", title));
            baseDir = directories().getPostsDraftData();
        } else {
            io().println(String.format("Create post '%s'...", title));
            baseDir = directories().getPostsData();
        }

        writeFile(createPath(baseDir, title), content);
    }

    /**
     * Write content to file.
     *
     * @param fileName must not be {@code null}
     * @param content must not be {@code null} or empty
     * @throws IOException if file can't be written
     */
    void writeFile(final Path fileName, final String content) throws IOException {
        io().println(String.format("Write file '%s'...", fileName));
        Files.write(
            Validate.notNull(fileName, "fileName"),
            Validate.notEmpty(content, "content").getBytes(configuration().getEncoding()));
    }

    /**
     * Create target path.
     *
     * @param baseDir must not be {@code null}
     * @param title must not be {@code null} or empty
     * @return never {@code null}
     */
    Path createPath(final Path baseDir, final String title) {
        return Validate.notNull(baseDir).resolve(createFileNameFromTitle(title));
    }

    /**
     * Create file name from title.
     * <p>
     * Format: {@literal TIME_TITLE.md} {@literal TIME} is provided by {@link #time}. {@literal TITLE} is the escaped
     * title.
     * </p>
     *
     * @param title must not be {@code null} or empty
     * @return never {@code null} or empty
     */
    String createFileNameFromTitle(final String title) {
        final StringBuilder buffer = new StringBuilder();

        buffer.append(time.nowAsString())
            .append('_')
            .append(StringEscape.escapeFileName(Validate.notEmpty(title)))
            .append(".md");

        return buffer.toString();
    }

    /**
     * Validates the required command line arguments.
     *
     * @throws ApplicationException if title is empty
     */
    @Override
    protected void validateArguments() throws ApplicationException {
        if (configuration().getTitle().isEmpty()) {
            throw new ApplicationException(ExitCodeImpl.TOO_FEW_ARGUMENTS, "No title arguemnt given!");
        }
    }
}
