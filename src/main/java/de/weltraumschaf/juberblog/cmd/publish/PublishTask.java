package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.Registry;
import de.weltraumschaf.juberblog.core.Pages;
import de.weltraumschaf.juberblog.core.PageType;
import de.weltraumschaf.juberblog.core.BaseTask;
import de.weltraumschaf.juberblog.core.Task;

/**
 * Task to publish pages.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class PublishTask extends BaseTask<Pages, Pages> implements Task<Pages, Pages> {

    private final PageType type;

    /**
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     * @param type must not be {@code null}
     */
    public PublishTask(final Registry registry, final PageType type) {
        super(Pages.class, registry);
        this.type = Validate.notNull(type, "type");
    }

    @Override
    public Pages execute() throws Exception {
        return execute(new Pages());
    }

    @Override
    public Pages execute(final Pages previusResult) throws Exception {
        final Publisher publisher = new Publisher(
            templates(),
            directories(),
            configuration(),
            type,
            version(),
            verbose()
        );

        previusResult.add(publisher.publish());
        return previusResult;
    }

}
