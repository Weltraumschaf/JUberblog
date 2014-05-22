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

package de.weltraumschaf.juberblog.files;

import de.weltraumschaf.commons.validate.Validate;
import java.io.File;

/**
 * Accept Markdown files.
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class HtmlFilenameFilter extends BaseFilenameFilter {

    /**
     * File extension of Markdown files.
     */
    private static final String FILE_EXTENSION = ".html";

    @Override
    public boolean accept(final File dir, final String name) {
        Validate.notNull(name, "Name must not be null!");
        return hasExtension(name, FILE_EXTENSION);
    }

}
