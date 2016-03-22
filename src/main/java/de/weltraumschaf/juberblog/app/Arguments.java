package de.weltraumschaf.juberblog.app;

import de.weltraumschaf.commons.validate.Validate;
import java.util.Arrays;

/**
 * Abstraction for CLI arguments.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
final class Arguments {

    /**
     * The arguments.
     */
    private final String[] args;

    /**
     * Creates empty arguments.
     *
     * Mostly for testing purposes.
     */
    Arguments() {
        this(new String[] {});
    }

    /**
     * Dedicated constructor.
     *
     * @param args must not be {@code null}, defense copied
     */
    Arguments(final String[] args) {
        Validate.notNull(args, "Argumets must not be null!");
        this.args = args.clone();
    }

    /**
     * Get the first argument.
     *
     * @return never {@code null}, maybe empty
     */
    String getFirstArgument() {
        return args.length == 0 ? "" : args[0];
    }

    /**
     * Get all arguments but the first.
     *
     * @return never {@code null}, maybe empty
     */
    String[] getTailArguments() {
        if (args.length < 2) {
            return new String[] {};
        }

        return Arrays.copyOfRange(args, 1, args.length);
    }

    /**
     * Get all arguments.
     *
     * @return never {@code null}, copy
     */
    String[] getAll() {
        return args.clone();
    }

    /**
     * Get number of arguments.
     *
     * @return [0 .. n]
     */
    int size() {
        return args.length;
    }

    /**
     * Whether there are arguments or not.
     *
     * @return {@code true} for no arguments at all, else {@code false}
     */
    boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public String toString() {
        return "first: " + getFirstArgument()
                + ", tail: " + Arrays.asList(getTailArguments()).toString();
    }

}
