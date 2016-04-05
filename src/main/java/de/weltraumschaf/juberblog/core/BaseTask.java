package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.validate.Validate;

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

    /**
     * Dedicated constructor.
     *
     * @param typeForPreviusResult must not be {@code null}
     */
    public BaseTask(final Class<P> typeForPreviusResult) {
        super();
        this.typeForPreviusResult = Validate.notNull(typeForPreviusResult, "typeForPreviusResult");
    }

    @Override
    public final Class<P> getDesiredTypeForPreviusResult() {
        return typeForPreviusResult;
    }

    /**
     * Encapsulates unsafe cast into single location to minimize impact of suppressing the warning.
     *
     * @param <T> generic type
     * @param aClass may be {@code null}
     * @return may be {@code null}
     */
    @SuppressWarnings("unchecked")
    static final <T> Class<T> castClass(final Class<?> aClass) {
        return (Class<T>) aClass;
    }

}
