/*
 *  LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 43):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a non alcohol-free beer in return.
 *
 * Copyright (C) 2012 "Sven Strittmatter" <weltraumschaf@googlemail.com>
 */

package de.weltraumschaf.juberblog.files;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.Environments;
import de.weltraumschaf.juberblog.Environments.Env;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Creates the directory for the environment variable {@literal $JUBERBLOG_HOME} if it does not exists.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class HomeDir {

    /**
     * Used to get environment variables.
     */
    private final Env env;

    /**
     * Convenience constructor which uses default environment.
     */
    public HomeDir() {
        this(Environments.defaultEnv());
    }

    /**
     * Dedicated constructor.
     *
     * @param env must not be {@code null}
     */
    public HomeDir(final Env env) {
        super();
        this.env = Validate.notNull(env, "enf");
    }

    /**
     * Creates the home directory if it does not exists yet.
     * <p>
     * If it already exists it is verified that it is readable, writable and a directory.
     * </p>
     *
     * @throws IOException if directory can't be created
     */
    public void createIfNotExists() throws IOException {
        final String pathName = env.get(Constants.ENVIRONMENT_VARIABLE_HOME.toString());
        final Path home = Paths.get(pathName);

        if (Files.exists(home)) {
            verify(home);
        } else {
            create(home);
        }
    }

    /**
     * Verifies that the home directory is readable/writable and a directory.
     * <p>
     * Throws {@link RuntimeException} if given path is not readable, writable or a directory.
     * </p>
     *
     * @param home must not be {@code null}
     */
    private void verify(final Path home) {
        if (!Files.isReadable(home)) {
            throw new RuntimeException(String.format("Home direcotry '%s' is not readable!", home));
        }

        if (!Files.isWritable(home)) {
            throw new RuntimeException(String.format("Home direcotry '%s' is not writeable!", home));
        }

        if (!Files.isDirectory(home)) {
            throw new RuntimeException(String.format("Home path '%s' is not a direcotry!", home));
        }
    }

    /**
     * Creates the directory.
     *
     * @param home must not be {@code null}
     * @throws IOException if directory can't be created
     */
    private void create(final Path home) throws IOException {
        Files.createDirectories(home);
    }

}
