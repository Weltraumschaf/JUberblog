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

/**
 * Types of installation strategies.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
enum InstallationType {

    /**
     * Requires an empty directory to install the files.
     */
    FRESH,
    /**
     * Existing files will be overwritten.
     */
    OVERWRITE,
    /**
     * Existing files will be backed up.
     */
    BACKUP;
}
