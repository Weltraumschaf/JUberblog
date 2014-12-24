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
package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.app.Options;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.core.Templates;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Registry object to cary around important objects.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
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
    private Configuration cfg;
    /**
     * Holds the command line options.
     */
    private Options opt;
    /**
     * Provides I/O for the command line.
     */
    private IO io;

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
    public Configuration configuration() {
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

    /**
     * Creates filled registry.
     *
     * @param cliOptions must not be {@code null}
     * @param io must not be {@code null}
     * @return never {@code null}
     * @throws ApplicationException if not all objects can't be generated
     */
    public static JUberblog generate(final Options cliOptions, final IO io) throws ApplicationException {
        final Configuration configuration = generateConfiguration(cliOptions);
        final Path locationDir = Paths.get(cliOptions.getLocation());

        if (!Files.isDirectory(locationDir)) {
            throw new ApplicationException(
                    ExitCodeImpl.FATAL,
                    String.format("Given location '%s' is not a valid direcotry!", cliOptions.getLocation()));
        }

        final Path dataDir = locationDir.resolve(configuration.getDataDir());
        final Path outputDir = locationDir.resolve(configuration.getHtdocs());
        final Path templateDir = locationDir.resolve(configuration.getTemplateDir());

        return JUberblog.Builder.create()
                .directories(
                        new Directories(
                                dataDir.resolve("posts"),
                                dataDir.resolve("sites"),
                                outputDir,
                                outputDir.resolve("posts"),
                                outputDir.resolve("sites")))
                .templates(
                        new Templates(
                                templateDir.resolve("layout.ftl"),
                                templateDir.resolve("post.ftl"),
                                templateDir.resolve("site.ftl"),
                                templateDir.resolve("feed.ftl"),
                                templateDir.resolve("index.ftl"),
                                templateDir.resolve("site_map.ftl")))
                .configuration(configuration)
                .options(cliOptions)
                .io(io)
                .product();
    }

    /**
     * Loads the configuration from given CLI option.
     *
     * @param cliOptions must not be {@code null}
     * @return never {@code null}, always new instance
     * @throws ApplicationException if file can't be loaded
     */
    private static Configuration generateConfiguration(final Options cliOptions) throws ApplicationException {
        final Path configFile = Paths.get(Validate.notNull(cliOptions, "cliOptions").getConfigurationFile());

        if (!Files.isRegularFile(configFile)) {
            throw new ApplicationException(
                    ExitCodeImpl.FATAL,
                    String.format("Can't read config file '%s'!", cliOptions.getConfigurationFile()));
        }

        final Configuration configuration;

        try {
            configuration = new Configuration(configFile.toString());
        } catch (final IOException ex) {
            throw new ApplicationException(
                    ExitCodeImpl.FATAL,
                    String.format("Error during read of config file '%s'!", cliOptions.getConfigurationFile()));
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
        public Builder configuration(final Configuration cfg) {
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

        /**
         * Creates the final product
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
