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

package de.weltraumschaf.juberblog.opt;

import com.beust.jcommander.Parameter;

/**
 * Common options for create and publish commands.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class CreateAndPublishOptions extends CommonOptions {

    /**
     * Configuration file argument.
     */
    @Parameter(names = {"-c", "--config" }, required = true, description = "Config file to use.")
    private String configurationFile = "";

    /**
     * Get configuration file.
     *
     * @return never {@code null}, maybe empty
     */
    public String getConfigurationFile() {
        return configurationFile;
    }

    @Override
    public String toString() {
        return super.toString() + ", configurationFile: " + configurationFile;
    }

}
