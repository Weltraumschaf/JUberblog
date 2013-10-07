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
package de.weltraumschaf.juberblog.cmd;

import com.google.common.collect.Maps;
import de.weltraumschaf.commons.IO;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.opt.InstallOptions;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

/**
 * Installation command.
 *
 * Creates a scaffold directory in specified location.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class InstallSubCommand extends BaseSubCommand<InstallOptions> {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(CreateSubCommand.class);
    private static final String SCAFFOLD = Constants.SCAFFOLD_PACKAGE.toString()
                                       .replace(".", Constants.DIR_SEP.toString());
    private static final String PREFIX = Constants.DIR_SEP.toString() + SCAFFOLD + Constants.DIR_SEP.toString();

    private InstallOptions options;

    /**
     * Dedicated constructor.
     *
     * @param options must not be {@code null}
     * @param io must not be {@code null}
     */
    public InstallSubCommand(final IO io) {
        super(io);
    }

    @Override
    protected void run() {
        final String target = options.getLocation();
        io.println(String.format("Install scaffold to '%s'...", target));
        final String[] paths = getClass().getResource(Constants.DIR_SEP.toString() + SCAFFOLD).toString().split("!");
        final String jarFile = paths[0];
        LOG.debug(jarFile);

        try {
            try (FileSystem fs = FileSystems.newFileSystem(URI.create(jarFile), Maps.<String, String>newHashMap())) {
                final String scaffold = paths[1];
                final Path dir = fs.getPath(scaffold);
                Files.walkFileTree(dir, new CopyDirectoryVisitor(new File(target).toPath(), PREFIX));
            }
        } catch (IOException ex) {
            io.errorln("Can't copy scaffold: " + ex.getMessage());
            io.printStackTrace(ex);
        }
    }

    @Override
    public void setOptions(final InstallOptions opt) {
        Validate.notNull(opt, "Options must not be null!");
        options = opt;
    }

    @Override
    public InstallOptions getOptions() {
        return options;
    }

    /**
     * Visitor to copy the scaffold directory.
     */
    private static class CopyDirectoryVisitor extends SimpleFileVisitor<Path> {

        /**
         * Log facility.
         */
        private static final Logger LOG = Logger.getLogger(CopyDirectoryVisitor.class);
        /**
         * Copy options.
         */
        private static final StandardCopyOption COPY_OPTIONS = StandardCopyOption.REPLACE_EXISTING;
        /**
         * Target directory.
         */
        private final Path targetDir;
        /**
         * Path prefix removed from package source file name.
         */
        private final String prefix;

        /**
         * Dedicated constructor.
         *
         * @param targetDir must not be {@code null}, must be directory, must exist
         * @param prefix must not be {@code null}
         */
        public CopyDirectoryVisitor(final Path targetDir, final String prefix) {
            super();
            Validate.notNull(targetDir, "Target dir must not be null!");

            if (!Files.exists(targetDir)) {
                throw new IllegalArgumentException(String.format("Target '%s' does not exists!", targetDir));
            }

            if (!Files.isDirectory(targetDir)) {
                throw new IllegalArgumentException(String.format("Target '%s' is not a directory!", targetDir));
            }

            this.targetDir = targetDir;
            Validate.notNull(prefix, "Prefix must not be null!");
            this.prefix = prefix;
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
                LOG.debug(String.format("Create directory %s...", dir));
                Files.createDirectory(target);
            }

            return result;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            final FileVisitResult result = super.visitFile(file, attrs);
            final Path target = targetDir.resolve(baseName(file));
            LOG.debug(String.format("Copy file %s to %s...", file, target));
            Files.copy(file, target, COPY_OPTIONS);
            return result;
        }

        private String baseName(final Path file) {
            return file.toString().replace(prefix, "");
        }

    }
}
