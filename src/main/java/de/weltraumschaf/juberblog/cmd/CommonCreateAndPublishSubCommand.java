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

import de.weltraumschaf.commons.ApplicationException;
import de.weltraumschaf.commons.IO;
import de.weltraumschaf.juberblog.BlogConfiguration;
import de.weltraumschaf.juberblog.Directories;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import de.weltraumschaf.juberblog.opt.CreateAndPublishOptions;
import java.io.IOException;
import org.apache.commons.lang3.Validate;

/**
 * Common functionality for {@link CreateSubCommand} and {@link PublishSubCommand}.
 *
 * @param <>> type of options
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
abstract class CommonCreateAndPublishSubCommand<T extends CreateAndPublishOptions> extends BaseSubCommand<T> {

    /**
     * Loaded from file.
     */
    private BlogConfiguration blogConfiguration;
    /**
     * Directories of the blog.
     */
    private Directories directories;

    /**
     * Dedicated constructor.
     *
     * @param io must not be {@literal null}
     */
    public CommonCreateAndPublishSubCommand(IO io) {
        super(io);
    }

    @Override
    protected void init() throws ApplicationException {
        super.init();
        loadBlogConfiguration();
        loadDirectories();
    }

    /**
     * Load the configuration file.
     *
     * @throws ApplicationException if configuration file can't be loaded
     */
    private void loadBlogConfiguration() throws ApplicationException {
        // TODO Argument validation.
        try {
            blogConfiguration = new BlogConfiguration(getOptions().getConfigurationFile());
            blogConfiguration.load();
        } catch (IOException | IllegalArgumentException | NullPointerException ex) {
            throw new ApplicationException(
                    ExitCodeImpl.CANT_LOAD_CONFIG,
                    String.format("Can't load configuration file '%s'!", getOptions().getConfigurationFile()),
                    ex);
        }
    }

    /**
     * Accessor for blog configuration provided by command line argument.
     *
     * @return never {@literal null}
     */
    protected BlogConfiguration getBlogConfiguration() {
        Validate.notNull(blogConfiguration, "Blog configuration must not be null!");
        return blogConfiguration;
    }

    /**
     * Load directories from configuration.
     */
    private void loadDirectories() {
        directories = new Directories(blogConfiguration);
    }

    /**
     * Accesor for directories.
     *
     * @return never {@literal null}
     */
    protected Directories getDirectories() {
        Validate.notNull(directories, "Directories must not be null!");
        return directories;
    }

}
