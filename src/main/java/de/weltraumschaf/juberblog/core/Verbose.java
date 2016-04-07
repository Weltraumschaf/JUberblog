package de.weltraumschaf.juberblog.core;

import java.io.PrintStream;

/**
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class Verbose {

    private final boolean enabled;
    private final PrintStream out;

    public Verbose(final boolean enabled, final PrintStream out) {
        super();
        this.enabled = enabled;
        this.out = out;
    }

    public void print(final String message, final Object... args) {
        if (enabled) {
            out.println(String.format(message, args));
        }
    }
}
