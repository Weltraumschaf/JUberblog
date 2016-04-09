package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.validate.Validate;

import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.options.InstallOptions;
import de.weltraumschaf.juberblog.options.Options;
import de.weltraumschaf.juberblog.options.OptionsWithConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Registry object to cary around important objects.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class JUberblog {

    /**
     * Holds important directories.
     */
    private Directories dirs;
    /**
     * Holds the needed templates.
     */
    private Templates tpls;
    /**
     * Holds the blog's configuration.
     */
    private BlogConfiguration cfg;
    /**
     * Holds the command line options.
     */
    private Options opt;
    /**
     * Provides I/O for the command line.
     */
    private IO io;
    private Version version;

    /**
     * Use {@link Builder} instead.
     */
    private JUberblog() {
        super();
    }

    /**
     * Copies the whole object.
     *
     * @return never {@code nul}, always new instance
     */
    private JUberblog copy() {
        final JUberblog copy = new JUberblog();
        copy.cfg = cfg;
        copy.dirs = dirs;
        copy.io = io;
        copy.opt = opt;
        copy.tpls = tpls;
        copy.version = version;
        return copy;
    }

    /**
     * Get the directories.
     *
     * @return never {@code null}
     */
    public Directories directories() {
        return dirs;
    }

    /**
     * Get the templates.
     *
     * @return never {@code null}
     */
    public Templates templates() {
        return tpls;
    }

    /**
     * Get the configuration.
     *
     * @return never {@code null}
     */
    public BlogConfiguration configuration() {
        return cfg;
    }

    /**
     * Get the options.
     *
     * @return never {@code null}
     */
    public Options options() {
        return opt;
    }

    /**
     * Get the I/O.
     *
     * @return never {@code null}
     */
    public IO io() {
        return io;
    }

    public Version version() {
        return version;
    }

    /**
     * Creates filled registry, but without loading configuration from file.
     *
     * @param cliOptions must not be {@code null}
     * @param io must not be {@code null}
     * @return never {@code null}
     * @throws ApplicationException if not all objects can't be generated
     */
    public static JUberblog generateWithDefaultConfig(final Options cliOptions, final IO io) throws ApplicationException {
        return generate(cliOptions, io, BlogConfiguration.DEFAULT);
    }

    /**
     * Creates filled registry with a given configuration.
     *
     * @param cliOptions must not be {@code null}
     * @param io must not be {@code null}
     * @param configuration must not be {@code null}
     * @return never {@code null}
     * @throws ApplicationException if not all objects can't be generated
     */
    public static JUberblog generate(final Options cliOptions, final IO io, final BlogConfiguration configuration) throws ApplicationException {
        final Path dataDir = findDataDir(configuration);
        final Path outputDir = findOutputDir(configuration);
        final Path templateDir = findTemplateDir(configuration);
        final Version version = new Version(Constants.PACKAGE_BASE.toString() + "/version.properties");

        try {
            version.load();
        } catch (IOException ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, "Can't load version properties!", ex);
        }

        return JUberblog.Builder.create()
            .directories(createDirs(dataDir, outputDir))
            .templates(createTemplate(templateDir))
            .configuration(configuration)
            .options(cliOptions)
            .io(io)
            .version(version)
            .product();
    }

    /**
     * Finds template directory based on location of blog.
     *
     * @param locationDir must not be {@code null}
     * @param configuration must not be {@code null}
     * @return never {@code null}
     */
    private static Path findTemplateDir(final BlogConfiguration configuration) {
        return Paths.get(configuration.getTemplateDir());
    }

    /**
     * Finds output directory based on location of blog.
     *
     * @param locationDir must not be {@code null}
     * @param configuration must not be {@code null}
     * @return never {@code null}
     */
    private static Path findOutputDir(final BlogConfiguration configuration) {
        return Paths.get(configuration.getHtdocs());
    }

    /**
     * Finds data directory based on location of blog.
     *
     * @param locationDir must not be {@code null}
     * @param configuration must not be {@code null}
     * @return never {@code null}
     */
    private static Path findDataDir(final BlogConfiguration configuration) {
        return Paths.get(configuration.getDataDir());
    }

    /**
     * Finds location directory of blog.
     *
     * @param cliOptions must not be {@code null}
     * @return never {@code null}
     * @throws ApplicationException if location is not a directory
     */
    private static Path findLocationDir(final InstallOptions cliOptions) throws ApplicationException {
        final Path locationDir = Paths.get(cliOptions.getLocation());

        if (!Files.isDirectory(locationDir)) {
            throw new ApplicationException(
                ExitCodeImpl.FATAL,
                String.format("Given location '%s' is not a valid direcotry!", cliOptions.getLocation()));
        }

        return locationDir;
    }

    /**
     * Creates template directory object.
     *
     * @param templateDir must not be {@code null}
     * @return never {@code null}
     */
    private static Templates createTemplate(final Path templateDir) {
        return new Templates(
            templateDir.resolve("layout.ftl"),
            templateDir.resolve("post.ftl"),
            templateDir.resolve("site.ftl"),
            templateDir.resolve("feed.ftl"),
            templateDir.resolve("index.ftl"),
            templateDir.resolve("site_map.ftl"),
            templateDir.resolve("create/post_or_site.md.ftl"));
    }

    /**
     * Creates directories object.
     *
     * @param dataDir must not be {@code null}
     * @param outputDir must not be {@code null}
     * @return never {@code null}
     */
    private static Directories createDirs(final Path dataDir, final Path outputDir) {
        return new Directories(dataDir, outputDir);
    }

    /**
     * Loads the configuration from given CLI option.
     *
     * @param cliOptions must not be {@code null}
     * @return never {@code null}, always new instance
     * @throws ApplicationException if file can't be loaded
     */
    public static BlogConfiguration generateConfiguration(final OptionsWithConfig cliOptions) throws ApplicationException {
        final Path configFile = Paths.get(Validate.notNull(cliOptions, "cliOptions").getConfig());

        if (!Files.isRegularFile(configFile)) {
            throw new ApplicationException(
                ExitCodeImpl.FATAL,
                String.format("Can't read config file '%s'!", cliOptions.getConfig()));
        }

        final BlogConfiguration configuration;

        try {
            configuration = new BlogConfiguration(configFile.toString());
        } catch (final IOException ex) {
            throw new ApplicationException(
                ExitCodeImpl.FATAL,
                String.format("Error during read of config file '%s'!", cliOptions.getConfig()));
        }

        return configuration;
    }

    /**
     * Builder for less constructor parameters.
     */
    public static final class Builder {

        /**
         * Holds the build values.
         */
        private final JUberblog holder = new JUberblog();

        /**
         * Use {@link #create()} instead.
         */
        private Builder() {
            super();
        }

        /**
         * Creates builder.
         *
         * @return never {@code null}, always new instance
         */
        public static Builder create() {
            return new Builder();
        }

        /**
         * Get the directories.
         *
         * @param dirs must not be {@code null}
         * @return never {@code null}
         */
        public Builder directories(final Directories dirs) {
            holder.dirs = Validate.notNull(dirs, "dirs");
            return this;
        }

        /**
         * Get the templates.
         *
         * @param tpls must not be {@code null}
         * @return never {@code null}
         */
        public Builder templates(final Templates tpls) {
            holder.tpls = Validate.notNull(tpls, "tpls");
            return this;
        }

        /**
         * Get the configuration.
         *
         * @param cfg must not be {@code null}
         * @return never {@code null}
         */
        public Builder configuration(final BlogConfiguration cfg) {
            holder.cfg = Validate.notNull(cfg, "cfg");
            return this;
        }

        /**
         * Get the options.
         *
         * @param opt must not be {@code null}
         * @return never {@code null}
         */
        public Builder options(final Options opt) {
            holder.opt = Validate.notNull(opt, "opt");
            return this;
        }

        /**
         * Get the I/O.
         *
         * @param io must not be {@code null}
         * @return never {@code null}
         */
        public Builder io(final IO io) {
            holder.io = Validate.notNull(io, "io");
            return this;
        }

        public Builder version(final Version version) {
            holder.version = Validate.notNull(version, "version");
            return this;
        }

        /**
         * Creates the final product.
         *
         * @return never {@code null}, always new instance
         */
        public JUberblog product() {
            return validate(holder.copy());
        }

        /**
         * Validates that all fields are not {@code null}.
         *
         * @param product must not be {@code null}
         * @return the validated object itself
         */
        private JUberblog validate(final JUberblog product) {
            Validate.notNull(product, "product");
            notNull(product.cfg, "Configuration must not be null! Call Builder#configuration(cfg) first.");
            notNull(product.dirs, "Directories must not be null! Call Builder#directories(dirs) first.");
            notNull(product.io, "I/O must not be null! Call Builder#io(io) first.");
            notNull(product.opt, "Options must not be null! Call Builder#options(opt) first.");
            notNull(product.tpls, "Templates must not be null! Call Builder#templates(tpls) first.");
            return product;
        }

        /**
         * Throws {@link NullPointerException} if passed in object is {@code null}.
         *
         * @param o may be {@code null}
         * @param msg the exception message
         */
        private void notNull(final Object o, final String msg) {
            if (null == o) {
                throw new NullPointerException(msg);
            }
        }
    }

}
