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
package de.weltraumschaf.juberblog.install;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.validate.Validate;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Visitor to copy the scaffold directory.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class CopyDirectoryVisitor extends SimpleFileVisitor<Path> {

    /**
     * Target directory.
     */
    private final Path targetDir;
    /**
     * Path prefix removed from package source file name.
     */
    private final String prefix;
    /**
     * Used for CLI IO.
     */
    private final IO io;
    /**
     * Whether to print verbose messages to IO.
     */
    private final boolean verbose;
    /**
     * File copy strategy.
     */
    private InstallationType strategy;

    /**
     * Dedicated constructor.
     *
     * @param targetDir must not be {@code null}, must be directory, must exist
     * @param prefixToStrip must not be {@code null}
     * @param io used for verbose out
     * @param verbose whether to be verbose or not
     * @param strategy must not be {@code null}
     */
    CopyDirectoryVisitor(
            final Path targetDir,
            final String prefixToStrip,
            final IO io,
            final boolean verbose,
            final InstallationType strategy) {
        super();
        Validate.notNull(targetDir, "targetDir");

        if (!Files.exists(targetDir)) {
            throw new IllegalArgumentException(String.format("Target '%s' does not exists!", targetDir));
        }

        if (!Files.isDirectory(targetDir)) {
            throw new IllegalArgumentException(String.format("Target '%s' is not a directory!", targetDir));
        }

        this.targetDir = targetDir;
        Validate.notNull(prefixToStrip, "prefixToStrip");
        this.prefix = prefixToStrip;
        this.io = Validate.notNull(io);
        this.verbose = verbose;
        this.strategy = Validate.notNull(strategy);
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        final FileVisitResult result = super.preVisitDirectory(dir, attrs);
        final String str = dir.toString() + "/";

        if (str.equals(prefix)) {
            // Do not create the root dir.
            return result;
        }

        final Path target = targetDir.resolve(baseName(dir));

        if (!Files.exists(target)) {
            println(String.format("Create directory %s", target));
            Files.createDirectory(target);
        }

        return result;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        final FileVisitResult result = super.visitFile(file, attrs);
        final Path target = targetDir.resolve(baseName(file));

        if (Files.exists(target)) {
            if (strategy == InstallationType.OVERWRITE) {
                println(String.format("Overwrite file %s ...", target));
                Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
            } else if (strategy == InstallationType.BACKUP) {
                println(String.format("Back up file %s ...", target));
                Files.copy(target, targetDir.resolve(baseName(file) + ".bak"));
                Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
            } else {
                throw new IllegalArgumentException(String.format("File '%s' already exists!", target));
            }
        } else {
            println(String.format("Copy file %s to %s ...", file, target));
            Files.copy(file, target);
        }

        return result;
    }

    /**
     * Strips prefix from given path.
     *
     * @param file must not be {@code null}
     * @return never {@code null}
     */
    private String baseName(final Path file) {
        return Validate.notNull(file, "file").toString().replace(prefix, "");
    }

    /**
     * Prints message only if {@link #verbose} is {@code true}.
     *
     * @param msg should not be {@code null} or empty
     */
    private void println(final String msg) {
        if (verbose) {
            io.println(msg);
        }
    }

}
