package de.weltraumschaf.juberblog.install;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Constants;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class installs the the scaffold file structure to a given directory.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
final class Scaffold {

    /**
     * Location of the scaffold directory with all default files and directories.
     */
    static final String RESOURCE_LOCATION = Constants.SCAFFOLD_PACKAGE
            .toString()
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
     * Behaviour type of installation.
     */
    private InstallationType type = InstallationType.FRESH;

    /**
     * Dedicated constructor.
     *
     * @param io must not be {@code null}
     */
    Scaffold(final IO io) {
        super();
        this.io = Validate.notNull(io, "Parameter 'io' mut not be null!");
    }

    /**
     * Whether to be verbose or not.
     *
     * @param verbose {@code true} for verbose output, else {@code false}
     */
    void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Injection point for a custom provider.
     *
     * @param srcJar must not be {@code null}
     */
    void setSrcJar(final SourceJarProvider srcJar) {
        this.srcJar = Validate.notNull(srcJar);
    }

    /**
     * Set installation behaviour type.
     *
     * @param type must not be {@code null}
     */
    void setType(final InstallationType type) {
        this.type = Validate.notNull(type, "type");
    }

    /**
     * Copy all files from scaffold to target directory.
     *
     * @param target must not be {@code null}
     * @throws IOException if scaffold files can't be copied
     */
    void copyFiles(final File target) throws IOException {
        Validate.notNull(target, "Target must not be null!");
        final JarResource src = JarResource.newSourceJar(srcJar.getAbsolutePath());

        try (final FileSystem fs = createJarFileSystem(URI.create(src.getJarLocation()))) {
            final String scaffold = src.getResourceLocation();
            final Path sourceDir = fs.getPath(scaffold);
            Files.walkFileTree(
                    sourceDir,
                    new CopyDirectoryVisitor(target.toPath(), STRIPPED_PREFIX, io, verbose, type));
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
     * Provides the {@link #RESOURCE_LOCATION scaffold resource path} inside an JAR file.
     */
    interface SourceJarProvider {

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

}
