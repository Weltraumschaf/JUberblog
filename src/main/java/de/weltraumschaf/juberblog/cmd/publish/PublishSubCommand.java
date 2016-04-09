package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.cmd.SubCommandBase;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.core.PageType;
import de.weltraumschaf.juberblog.core.TaskExecutor;
import de.weltraumschaf.juberblog.core.Verbose;
import org.joda.time.DateTime;

/**
 * Published the blog (pages, sites, index, site map, feed).
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class PublishSubCommand extends SubCommandBase {

    private final Verbose verbose;

    /**
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     */
    public PublishSubCommand(final JUberblog registry) {
        super(registry);
        verbose = new Verbose(registry.options().isVerbose(), registry.io().getStdout());
    }

    @Override
    protected void doExecute() throws ApplicationException {
        verbose.print("Publish blog...");
        final TaskExecutor executor = new TaskExecutor()
            .append(new PublishTask(new PublishTask.Config(
                        templates(),
                        directories(),
                        configuration(),
                        PageType.POST,
                        version()
                    ), verbose))
            .append(new GenerateFeedTask(new GenerateFeedTask.Config(
                        templates(),
                        directories(),
                        configuration(),
                        new DateTime("2014-12-08T20:17:00") // FIXME Change this.
                    ), verbose))
            .append(new GenerateIndexTask(new GenerateIndexTask.Config(
                        templates(),
                        directories(),
                        configuration(),
                        version()
                    ), verbose))
            .append(new PublishTask(new PublishTask.Config(
                        templates(),
                        directories(),
                        configuration(),
                        PageType.SITE,
                        version()
                    ), verbose))
            .append(new GenerateSitemapTask(new GenerateSitemapTask.Config(
                        templates(),
                        directories(),
                        configuration()
                    ), verbose));
        try {
            executor.execute();
        } catch (Exception ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, ex.getMessage(), ex);
        }
    }

}
