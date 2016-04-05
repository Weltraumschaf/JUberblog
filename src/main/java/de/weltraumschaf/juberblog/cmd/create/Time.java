package de.weltraumschaf.juberblog.cmd.create;

/**
 * Factory to provide time utilities.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class Time {

    /**
     * Hidden for pure static factory.
     */
    private Time() {
        super();
        throw new UnsupportedOperationException("Do not call by reflection!");
    }

    /**
     * Creates a new time provider.
     *
     * @return never {@code null}, always new instance
     */
    public static TimeProvider newProvider() {
        return new DefaultTimeProvider();
    }
}
