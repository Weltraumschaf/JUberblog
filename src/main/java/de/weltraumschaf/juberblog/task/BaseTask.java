package de.weltraumschaf.juberblog.task;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.juberblog.Registry;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.core.Verbose;
import de.weltraumschaf.juberblog.options.Options;
import de.weltraumschaf.juberblog.task.Task;

/**
 * Common functionality for all tasks.
 *
 * @param <R> type of return value
 * @param <P> type of previous result
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public abstract class BaseTask<R, P> implements Task<R, P>, Registry {

    /**
     * Token type class of result from previous task.
     */
    private final Class<P> typeForPreviousResult;
    private final Registry registry;

    /**
     * Dedicated constructor.
     *
     * @param typeForPreviousResult must not be {@code null}
     * @param registry must not be {@code null}
     */
    public BaseTask(final Class<P> typeForPreviousResult, final Registry registry) {
        super();
        this.typeForPreviousResult = Validate.notNull(typeForPreviousResult, "typeForPreviousResult");
        this.registry = Validate.notNull(registry, "registry");
    }

    @Override
    public final Class<P> getDesiredTypeForPreviousResult() {
        return typeForPreviousResult;
    }

    protected final void println(final String message, final Object... args) {
        registry.verbose().print(message, args);
    }

    @Override
    public final Options options() {
        return registry.options();
    }

    @Override
    public final IO io() {
        return registry.io();
    }

    @Override
    public final Templates templates() {
        return registry.templates();
    }

    @Override
    public final Directories directories() {
        return registry.directories();
    }

    @Override
    public final BlogConfiguration configuration() {
        return registry.configuration();
    }

    @Override
    public final Version version() {
        return registry.version();
    }

    @Override
    public final FreeMarkerDown fmd() {
        return registry.fmd();
    }

    @Override
    public final Verbose verbose() {
        return registry.verbose();
    }



}
