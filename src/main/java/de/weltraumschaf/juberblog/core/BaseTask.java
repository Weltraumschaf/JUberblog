package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.juberblog.Registry;
import de.weltraumschaf.juberblog.options.Options;

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
    private final Class<P> typeForPreviusResult;
    private final Registry registry;

    /**
     * Dedicated constructor.
     *
     * @param typeForPreviusResult must not be {@code null}
     * @param registry must not be {@code null}
     */
    public BaseTask(final Class<P> typeForPreviusResult, final Registry registry) {
        super();
        this.typeForPreviusResult = Validate.notNull(typeForPreviusResult, "typeForPreviusResult");
        this.registry = Validate.notNull(registry, "registry");
    }

    @Override
    public final Class<P> getDesiredTypeForPreviusResult() {
        return typeForPreviusResult;
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
