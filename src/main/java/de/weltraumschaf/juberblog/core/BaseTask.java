package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.validate.Validate;
import java.io.PrintStream;

/**
 * Common functionality for all tasks.
 *
 * @param <R> type of return value
 * @param <P> type of previous result
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public abstract class BaseTask<R, P> implements Task<R, P> {

    /**
     * Token type class of result from previous task.
     */
    private final Class<P> typeForPreviusResult;
    private final Verbose verbose;

    /**
     * Dedicated constructor.
     *
     * @param typeForPreviusResult must not be {@code null}
     * @param verbose must not be {@code null}
     */
    public BaseTask(final Class<P> typeForPreviusResult, final Verbose verbose) {
        super();
        this.typeForPreviusResult = Validate.notNull(typeForPreviusResult, "typeForPreviusResult");
        this.verbose = Validate.notNull(verbose, "verbose");
    }

    @Override
    public final Class<P> getDesiredTypeForPreviusResult() {
        return typeForPreviusResult;
    }

    protected final void println(final String message, final Object... args) {
        verbose.print(message, args);
    }

    protected Verbose getVerbose() {
        return verbose;
    }

}
