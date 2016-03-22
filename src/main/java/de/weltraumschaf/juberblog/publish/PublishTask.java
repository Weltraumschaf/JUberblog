package de.weltraumschaf.juberblog.publish;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Page.Pages;
import de.weltraumschaf.juberblog.core.Page.Type;
import de.weltraumschaf.juberblog.core.BaseTask;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Task;
import de.weltraumschaf.juberblog.core.Templates;

/**
 * Task to publish pages.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class PublishTask extends BaseTask<Pages, Pages> implements Task<Pages, Pages> {

    /**
     * Task configuration.
     */
    private final Config config;

    /**
     * Dedicated constructor.
     *
     * @param config must not be {@code null}
     */
    public PublishTask(final Config config) {
        super(Pages.class);
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Pages execute() throws Exception {
        return execute(new Pages());
    }

    @Override
    public Pages execute(final Pages previusResult) throws Exception {
        final Publisher publisher = new Publisher(
                config.templates,
                config.directories,
                config.configuration,
                config.type
        );

        previusResult.addAll(publisher.publish());
        return previusResult;
    }

    /**
     * Task configuration.
     */
    public static final class Config {

       /**
        * Where to find template files.
        */
        private final Templates templates;
        /**
         * Where to find important directories.
         */
        private final Directories directories;
        /**
         * The blog configuration.
         */
        private final Configuration configuration;
        /**
         * Type of published data.
         */
        private final Type type;

        /**
         * Dedicated constructor.
         *
         * @param templates must not be {@code null}
         * @param directories must not be {@code null}
         * @param configuration must not be {@code null}
         * @param type must not be {@code null}
         */
        public Config(
                final Templates templates,
                final Directories directories,
                final Configuration configuration,
                final Type type) {
            super();
            this.templates = Validate.notNull(templates, "templates");
            this.directories = Validate.notNull(directories, "directories");
            this.configuration = Validate.notNull(configuration, "configuration");
            this.type = Validate.notNull(type, "type");
        }

    }
}
