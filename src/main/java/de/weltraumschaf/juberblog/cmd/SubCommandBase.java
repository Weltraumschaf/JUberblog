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

import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.app.Options;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;

/**
 * Common functionality for sub commands.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class SubCommandBase implements SubCommand {

    /**
     * Provides some important objects.
     */
    private final JUberblog registry;

    /**
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     */
    public SubCommandBase(final JUberblog registry) {
        super();
        this.registry = Validate.notNull(registry, "registry");
    }

    @Override
    public final Options options() {
        return registry().options();
    }

    @Override
    public final IO io() {
        return registry().io();
    }

    @Override
    public final Templates templates() {
        return registry().templates();
    }

    @Override
    public final Directories directories() {
        return registry().directories();
    }

    @Override
    public final Configuration configuration() {
        return registry().configuration();
    }

    /**
     * Getter for registry.
     *
     * @return never {@code null}
     */
    private JUberblog registry() {
        return registry;
    }
}
