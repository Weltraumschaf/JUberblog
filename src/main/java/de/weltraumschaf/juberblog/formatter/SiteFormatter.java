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

package de.weltraumschaf.juberblog.formatter;

import freemarker.template.Configuration;
import java.io.IOException;

/**
 * Formats a site.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class SiteFormatter extends BaseFormatter {

    /**
     * Site template file name.
     */
    private static final String SITE_TEMPLATE = "site.ftl";

    /**
     * Dedicated constructor.
     * 
     * @param templateConfiguration must not be {@code null} or empty
     * @throws IOException if template file can't be read
     */
    public SiteFormatter(final Configuration templateConfiguration, final String markdown) throws IOException {
        super(templateConfiguration, SITE_TEMPLATE, markdown);
    }


}
