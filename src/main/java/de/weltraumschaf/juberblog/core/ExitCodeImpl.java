package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.system.ExitCode;

/**
 * Exit codes for application.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public enum ExitCodeImpl implements ExitCode {

    /**
     * Any unspecified error.
     */
    FATAL(-1),
    /**
     * No errors.
     */
    OK(0),
    /**
     * Unspecified fatal error occurred.
     */
    UNKNOWN_COMMAND(1),
    /**
     * Too few command line arguments.
     */
    TOO_FEW_ARGUMENTS(2),
    /**
     * Can't load configuration file.
     */
    CANT_LOAD_CONFIG(3),
    /**
     * Indicates a missing command line argument.
     */
    MISSING_ARGUMENT(4),
    /**
     * Indicates an invalid command line argument.
     */
    BAD_ARGUMENT(5),
    /**
     * If on start up system.in, system.out or System.err can't be accessed.
     */
    CANT_READ_IO_STREAMS(255);

    /**
     * Exit code number returned as exit code to JVM.
     */
    private final int code;

    /**
     * Dedicated constructor.
     *
     * @param code exit code number
     */
    ExitCodeImpl(final int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
