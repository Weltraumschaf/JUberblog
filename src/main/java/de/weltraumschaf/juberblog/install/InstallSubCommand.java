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

import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.cmd.SubCommandBase;

/**
 * Installs a fresh blog.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class InstallSubCommand  extends SubCommandBase {

    /**
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     */
    public InstallSubCommand(final JUberblog registry) {
        super(registry);
    }

    @Override
    public void execute() {
        io().println("install"); // TODO Implement install sub command.
    }
}
