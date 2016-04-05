package de.weltraumschaf.juberblog.cmd.create;

/**
 * Provides time.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public interface TimeProvider {

    /**
     * Returns now in form of {@literal yyyy-mm-ddThh.mm.ss}.
     *
     * @return never {@code null} or empty
     */
    String nowAsString();
}
