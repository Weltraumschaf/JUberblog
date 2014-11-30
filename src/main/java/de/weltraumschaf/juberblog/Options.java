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

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Options {

    @Parameter(names = {"-d", "--debug"})
    private boolean debug;
    @Parameter(names = {"-h", "--help"})
    private boolean help;
    @Parameter(names = {"-v", "--version"})
    private boolean version;
    /**
     * Not used, only for suppressing error about main parameter.
     */
    @Parameter
    private List<String> rest = new ArrayList<>();

    boolean isDebug() {
        return debug;
    }

    boolean isHelp() {
        return help;
    }

    boolean isVersion() {
        return version;
    }

}
