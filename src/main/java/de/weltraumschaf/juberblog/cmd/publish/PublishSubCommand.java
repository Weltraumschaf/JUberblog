package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.cmd.SubCommandBase;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.core.PageType;
import de.weltraumschaf.juberblog.core.TaskExecutor;
import org.joda.time.DateTime;

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
        final TaskExecutor executor = new TaskExecutor()
            .append(new PublishTask(new PublishTask.Config(
                        templates(),
                        directories(),
                        configuration(),
                        PageType.POST
                    )))
            .append(new GenerateFeedTask(new GenerateFeedTask.Config(
                        templates(),
                        directories(),
                        configuration(),
                        new DateTime("2014-12-08T20:17:00")
                    )))
            .append(new GenerateIndexTask(new GenerateIndexTask.Config(
                        templates(),
                        directories(),
                        configuration()
                    )))
            .append(new PublishTask(new PublishTask.Config(
                        templates(),
                        directories(),
                        configuration(),
                        PageType.SITE
                    )))
            .append(new GenerateSitemapTask(new GenerateSitemapTask.Config(
                        templates(),
                        directories(),
                        configuration())));
        try {
            executor.execute();
        } catch (Exception ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, ex.getMessage(), ex);
        }
    }

    @Override
    protected void validateArguments() throws ApplicationException {
    }

}
