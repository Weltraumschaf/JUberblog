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
package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.model.PublishedPages;

/**
 * Generates the home site.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class HomeSiteGenerator implements Command {

    /**
     * Contains data to generate the home site.
     */
    private final PublishedPages pages;

    /**
     * Dedicated constructor.
     *
     * @param pages
     */
    public HomeSiteGenerator(final PublishedPages pages) {
        super();
        this.pages = Validate.notNull(pages, "pages");
    }

    @Override
    public void execute() {
        // TODO Implementhome home site generation.
    }

}
