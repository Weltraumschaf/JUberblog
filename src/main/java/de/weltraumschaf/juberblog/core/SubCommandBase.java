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

package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.validate.Validate;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class SubCommandBase implements SubCommand {

    private final JUberblog registry;

    public SubCommandBase(final JUberblog registry) {
        super();
        this.registry = Validate.notNull(registry, "registry");
    }

    @Override
    public final Options options() {
        return registry.options();
    }

    @Override
    public final IO io() {
        return registry.io();
    }

    @Override
    public JUberblog registry() {
        return registry;
    }

}
