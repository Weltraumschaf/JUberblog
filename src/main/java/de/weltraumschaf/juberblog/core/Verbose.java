package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.validate.Validate;

import java.io.PrintStream;
import java.util.Locale;

/**
 * Abstraction for verbose output.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class Verbose {

    /**
     * Whether verbose output is enabled or not.
     */
    private final boolean enabled;
    /**
     * The verbose output stream.
     */
    private final PrintStream out;

    /**
     * Dedicated constructor.
     *
     * @param enabled whether verbose output is enabled or nto
     * @param out must not be {@code null}
     */
    public Verbose(final boolean enabled, final PrintStream out) {
        super();
        this.enabled = enabled;
        this.out = Validate.notNull(out, "out");
    }

    /**
     * Print some verbose output.
     * <p>
     *     This method understands format strimgs like {@link String#format(Locale, String, Object...)} does.
     * </p>
     * @param message the output message format, must not be {@code null}
     * @param args optional format arguments.
     */
    public void print(final String message, final Object... args) {
        if (enabled) {
            out.println(String.format(message, args));
        }
    }
}
