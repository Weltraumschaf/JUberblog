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
package de.weltraumschaf.juberblog.cmd.install;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import de.weltraumschaf.juberblog.cmd.BaseSubCommand;
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
import org.apache.log4j.Logger;

/**
 * Installation command.
 *
 * Creates a scaffold directory in specified location.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class InstallSubCommand extends BaseSubCommand<InstallOptions> {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(InstallSubCommand.class);
    /**
     * Location of the scaffold directory with all default files and directories.
     */
    static final String SCAFFOLD = Constants.SCAFFOLD_PACKAGE.toString()
            .replace(".", Constants.DIR_SEP.toString());
    /**
     * Prefix to strip of from resource name.
     */
    private static final String STRIPPED_PREFIX = Constants.DIR_SEP.toString() + SCAFFOLD + Constants.DIR_SEP.toString();
    private SourceJarProvider srcJar = new DefaultSourceJarProvider();

    /**
     * Command line options.
     */
    private InstallOptions options;

    /**
     * Dedicated constructor.
     *
     * @param io must not be {@code null}
     */
    public InstallSubCommand(final IO io) {
        super(io);
    }

    void setSrcJar(final SourceJarProvider srcJar) {
        this.srcJar = Validate.notNull(srcJar);
    }

    @Override
    protected void run() throws ApplicationException {
        if (getOptions().isHelp()) {
            return;
        }

        final String location = options.getLocation().trim();
        final File target = validateLocation(location);
        io.println(String.format("Install scaffold to '%s'...", location));
        copyFiles(target);
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
     * Copy all files from scaffold to target directory.
     *
     * @param target must not be {@code null}
     */
    private void copyFiles(final File target) {
        Validate.notNull(target, "Target must not be null!");
        final SourceJar src = SourceJar.newSourceJar(srcJar.getAbsolutePath());

        try {
            try (FileSystem fs = createJarFileSystem(URI.create(src.getJarLocation()))) {
                final String scaffold = src.getResourceLocation();
                final Path dir = fs.getPath(scaffold);
                Files.walkFileTree(dir,
                        new CopyDirectoryVisitor(target.toPath(), STRIPPED_PREFIX, io, getOptions().isVerbose()));
            }
        } catch (IOException ex) {
            io.errorln("Can't copy scaffold: " + ex.getMessage());
            io.printStackTrace(ex);
        }
    }

    private FileSystem createJarFileSystem(final URI jarFileLocation) throws IOException {
        return FileSystems.newFileSystem(jarFileLocation, Maps.<String, String>newHashMap());
    }

    /**
     * Validate instalation directory.
     *
     * Validates that given string is not empty and is an existing directory.
     *
     * @param location must not be {@code null} or empty
     * @return never {@code null}
     * @throws ApplicationException if target does not exist or is not a directory
     */
    private File validateLocation(final String location) throws ApplicationException {
        Validate.notNull(location, "Location must not be null!");

        if (location.isEmpty()) {
            throw new ApplicationException(
                    ExitCodeImpl.MISSING_ARGUMENT,
                    "Empty location given! Please specify a valid direcotry as installation location.",
                    null);
        }

        final File target = new File(location);

        if (!target.exists()) {
            throw new ApplicationException(
                    ExitCodeImpl.BAD_ARGUMENT,
                    String.format("Install location '%s' does not exist!", location),
                    null);
        }

        if (!target.isDirectory()) {
            throw new ApplicationException(
                    ExitCodeImpl.BAD_ARGUMENT,
                    String.format("Install location '%s' is not a directory!", location),
                    null);
        }

        return target;
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
        private final IO io;
        private final boolean verbose;

        /**
         * Dedicated constructor.
         *
         * @param targetDir must not be {@code null}, must be directory, must exist
         * @param prefixToStrip must not be {@code null}
         */
        public CopyDirectoryVisitor(final Path targetDir, final String prefixToStrip, final IO io, final boolean verbose) {
            super();
            Validate.notNull(targetDir, "Target dir must not be null!");

            if (!Files.exists(targetDir)) {
                throw new IllegalArgumentException(String.format("Target '%s' does not exists!", targetDir));
            }

            if (!Files.isDirectory(targetDir)) {
                throw new IllegalArgumentException(String.format("Target '%s' is not a directory!", targetDir));
            }

            this.targetDir = targetDir;
            Validate.notNull(prefixToStrip, "Prefix must not be null!");
            this.prefix = prefixToStrip;
            this.io = Validate.notNull(io);
            this.verbose = verbose;
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
                if (verbose) {
                    io.println(String.format("Create directory %s", target));
                }

                Files.createDirectory(target);
            }

            return result;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            final FileVisitResult result = super.visitFile(file, attrs);
            final Path target = targetDir.resolve(baseName(file));

            if (verbose) {
                io.println(String.format("Copy file %s to %s", file, target));
            }

            Files.copy(file, target, COPY_OPTIONS);

            return result;
        }

        /**
         * Strips prefix from given path.
         *
         * @param file must not be {@code null}
         * @return never {@code null}
         */
        private String baseName(final Path file) {
            Validate.notNull(file, "File must not be null!");
            return file.toString().replace(prefix, "");
        }

    }

    /**
     * Produces something like {@literal jar:file:/Users/sxs/src/java/JUberblog/bin/juberblog.jar!/de/weltraumschaf/juberblog/scaffold}.
     */
    interface SourceJarProvider {
        String getAbsolutePath();
    }

    private static final class DefaultSourceJarProvider implements SourceJarProvider {

        @Override
        public String getAbsolutePath() {
            return getClass().getResource(Constants.DIR_SEP.toString() + SCAFFOLD).toString();
        }
    }

    static final class SourceJar {
        private final String jarLocation;
        private final String resourceLocation;

        SourceJar(final String jarLocation, final String resourceLocation) {
            super();
            this.jarLocation = Validate.notEmpty(jarLocation);
            this.resourceLocation = Validate.notEmpty(resourceLocation);
        }

        String getJarLocation() {
            return jarLocation;
        }

        String getResourceLocation() {
            return resourceLocation;
        }

        static SourceJar newSourceJar(final String path) {
            Validate.notEmpty(path, "Parameter 'path' must not be null or empty!");

            if (!path.contains("!")) {
                throw new IllegalArgumentException("Path does not contain '!'!");
            }

            final String[] paths = path.split("!");

            if (paths.length < 2) {
                throw new IllegalArgumentException("The string after the '!' must not be null or empty!");
            }

            return new SourceJar(
                Validate.notEmpty(paths[0], "The string before the '!' must not be null or empty!"),
                Validate.notEmpty(paths[1], "The string after the '!' must not be null or empty!"));
        }
    }
}
