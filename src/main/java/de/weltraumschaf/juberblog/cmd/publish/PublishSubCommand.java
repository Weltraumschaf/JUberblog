package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.cmd.SubCommandBase;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.core.PageType;
import de.weltraumschaf.juberblog.core.TaskExecutor;

/**
 * Published the blog (pages, sites, index, site map, feed).
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class PublishSubCommand extends SubCommandBase {

    /**
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     */
    public PublishSubCommand(final JUberblog registry) {
        super(registry);
    }

    @Override
    protected void doExecute() throws ApplicationException {
        verbose().print("Publish blog...");
        final TaskExecutor executor = new TaskExecutor()
            .append(new PublishTask(registry(), PageType.POST))
            .append(new GenerateFeedTask(registry()))
            .append(new GenerateIndexTask(registry()))
            .append(new PublishTask(registry(), PageType.SITE))
            .append(new GenerateSitemapTask(registry()));

        try {
            executor.execute();
        } catch (Exception ex) {
            final String message = ex.getMessage() == null ? "n/a" : ex.getMessage();
            throw new ApplicationException(ExitCodeImpl.FATAL, message, ex);
        }
    }

}
