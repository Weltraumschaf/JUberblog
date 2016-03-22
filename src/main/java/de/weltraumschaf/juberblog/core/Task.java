package de.weltraumschaf.juberblog.core;

/**
 * A code unit which does something.
 *
 * @param <R> type of return value
 * @param <P> type of previous result
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public interface Task<R, P> {

    /**
     * Executes the task.
     *
     * @return may return {@code null}
     * @throws Exception if any error happens
     */
    R execute() throws Exception;

    /**
     * Executes the task and takes the result of the previously executed task.
     *
     * @param previusResult may be {@code null}
     * @return may return {@code null}
     * @throws Exception if any error happens
     */
    R execute(P previusResult) throws Exception;

    /**
     * Get the desired type of consumed previous result (token type pattern).
     *
     * @return never {@code null}
     */
    Class<P> getDesiredTypeForPreviusResult();

}
