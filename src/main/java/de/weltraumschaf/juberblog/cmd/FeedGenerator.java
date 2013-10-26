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

/**
 * Generates the feed XML.
 *
 * 1. read all files in posts/ with mod date
 * 2. generate feed object from files
 * 3. write feed.xml
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class FeedGenerator implements Command {

    @Override
    public void execute() {
        // TODO Implementhome feed generation.
    }

}
