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

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.Constants;
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

/**
 * This class installs the the scaffold file structure to a given directory.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Scaffold {

    /**
     * Location of the scaffold directory with all default files and directories.
     */
    static final String RESOURCE_LOCATION = Constants.SCAFFOLD_PACKAGE.toString()
            .replace(".", Constants.DIR_SEP.toString());
    /**
     * Prefix to strip of from resource name.
     */
    private static final String STRIPPED_PREFIX = Constants.DIR_SEP.toString()
            + RESOURCE_LOCATION + Constants.DIR_SEP.toString();
    /**
     * Used for CLI IO.
     */
    private final IO io;
    /**
     * Whether to print verbose messages to IO.
     */
    private boolean verbose;
    /**
     * Provides the source JAR file location.
     */
    private SourceJarProvider srcJar = new DefaultSourceJarProvider();

    /**
     * Dedicated constructor.
     *
     * @param io must not be {@code null}
     */
    public Scaffold(final IO io) {
        super();
        this.io = Validate.notNull(io, "Parameter 'io' mut not be null!");
    }

    /**
     * Whether to be verbose or not.
     *
     * @param verbose {@code true} for verbose output, else {@code false}
     */
    void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Injection point for a custom provider.
     *
     * @param srcJar must not be {@code null}
     */
    public void setSrcJar(final SourceJarProvider srcJar) {
        this.srcJar = Validate.notNull(srcJar);
    }

    /**
     * Copy all files from scaffold to target directory.
     *
     * @param target must not be {@code null}
     * @throws IOException if scaffold files can't be copied
     */
    public void copyFiles(final File target) throws IOException {
        Validate.notNull(target, "Target must not be null!");
        final JarResource src = JarResource.newSourceJar(srcJar.getAbsolutePath());

        try (FileSystem fs = createJarFileSystem(URI.create(src.getJarLocation()))) {
            final String scaffold = src.getResourceLocation();
            final Path dir = fs.getPath(scaffold);
            Files.walkFileTree(dir, new CopyDirectoryVisitor(target.toPath(), STRIPPED_PREFIX, io, verbose));
        }
    }

    /**
     * Creates a file system for a given JAR file.
     *
     * @param jarFileLocation must not be {@code null}
     * @return never {@code null}
     * @throws IOException if file system can't be opened
     */
    private FileSystem createJarFileSystem(final URI jarFileLocation) throws IOException {
        return FileSystems.newFileSystem(Validate.notNull(jarFileLocation), Maps.<String, String>newHashMap());
    }

    /**
     * Visitor to copy the scaffold directory.
     */
    private static class CopyDirectoryVisitor extends SimpleFileVisitor<Path> {

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
         * Used for CLI IO.
         */
        private final IO io;
        /**
         * Whether to print verbose messages to IO.
         */
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
     * Provides the {@link #RESOURCE_LOCATION scaffold resource path} inside an JAR file.
     */
    public interface SourceJarProvider {

        /**
         * Produces something like Produces something like
         * {@literal jar:file:/home/foo/JUberblog/bin/juberblog.jar!/de/weltraumschaf/juberblog/scaffold}.
         *
         * @return never {@code null} or empty
         */
        String getAbsolutePath();
    }

    /**
     * Default provider which gives the final jar's path.
     */
    private static final class DefaultSourceJarProvider implements SourceJarProvider {

        @Override
        public String getAbsolutePath() {
            return getClass().getResource(Constants.DIR_SEP.toString() + RESOURCE_LOCATION).toString();
        }
    }

    /**
     * A JAR resource consists of the absolute path of the JAR file and the location of the resource inside the JAR.
     */
    static final class JarResource {

        /**
         * Location of the JAR file.
         */
        private final String jarLocation;
        /**
         * Location of resource inside JAR file.
         */
        private final String resourceLocation;

        /**
         * Dedicated constructor.
         *
         * @param jarLocation must not be {@code null} or empty
         * @param resourceLocation must not be {@code null} or empty
         */
        JarResource(final String jarLocation, final String resourceLocation) {
            super();
            this.jarLocation = Validate.notEmpty(jarLocation);
            this.resourceLocation = Validate.notEmpty(resourceLocation);
        }

        /**
         * Get the JAR file location.
         *
         * @return never {@code null} or empty
         */
        String getJarLocation() {
            return jarLocation;
        }

        /**
         * Get the resource location inside JAR.
         *
         * @return never {@code null} or empty
         */
        String getResourceLocation() {
            return resourceLocation;
        }

        /**
         * Create a resource from a given path.
         * <p>
         * A path should have the format: {@literal jar:file/path/to/jar/file.jar!/package/in/the/jar/File.class}.
         * </p>
         *
         * @param path must not be {@code null} or empty
         * @return never {@code null}
         */
        static JarResource newSourceJar(final String path) {
            Validate.notEmpty(path, "Parameter 'path' must not be null or empty!");

            if (!path.contains("!")) {
                throw new IllegalArgumentException("Path does not contain '!'!");
            }

            final String[] paths = path.split("!");

            if (paths.length < 2) {
                throw new IllegalArgumentException("The string after the '!' must not be null or empty!");
            }

            return new JarResource(
                    Validate.notEmpty(paths[0], "The string before the '!' must not be null or empty!"),
                    Validate.notEmpty(paths[1], "The string after the '!' must not be null or empty!"));
        }
    }
}
