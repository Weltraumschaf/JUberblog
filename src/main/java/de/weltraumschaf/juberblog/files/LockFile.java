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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Creates/removes a file at any location and provides a method to check the presence of it.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class LockFile {

    /**
     * Path of the lock file.
     */
    private final Path lockFile;

    /**
     * Dedicated constructor.
     *
     * @param lockFile must not be {@code null}
     */
    public LockFile(final Path lockFile) {
        super();
        this.lockFile = Validate.notNull(lockFile, "lockFile");
    }

    /**
     * Creates the lock file.
     * <p>
     * Throws {@link IllegalStateException} if lock file is already created.
     * </p>
     *
     * @throws IOException if lock file can't be created
     */
    public void create() throws IOException {
        if (exists()) {
            throw new IllegalStateException(String.format("Lock file '%s' already exists!", lockFile));
        }

        Files.createFile(lockFile);
    }

    /**
     * Removes the lock file.
     * <p>
     * Throws {@link IllegalStateException} if file is already removed.
     * </p>
     *
     * @throws IOException if lock file can't be created
     */
    public void remove() throws IOException {
        if (!exists()) {
            throw new IllegalStateException(String.format("Lock file '%s' does not exists!", lockFile));
        }

        Files.delete(lockFile);
    }

    /**
     * Whether the lock file exists or not.
     *
     * @return {@code true} if it exists, else {@code false}
     */
    public boolean exists() {
        return Files.exists(lockFile);
    }

    /**
     * Get the path of the lock file.
     *
     * @return never {@code null}
     */
    public Path getPath() {
        return lockFile;
    }
}
