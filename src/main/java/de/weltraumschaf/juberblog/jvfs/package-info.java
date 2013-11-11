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

/**
 * A virtual in-memory file system.
 *
 * Mostly inspired by the <a href="http://arquillian.org/guides/shrinkwrap_introduction/">Shrinkwrap</a>
 * project and therefore some code is copied from there.
 *
 * Resources:
 * - http://jaxenter.de/artikel/javaniofile-Zeitgemaesses-Arbeiten-mit-Dateien-166848
 * - http://exitcondition.alrubinger.com/2012/08/17/shrinkwrap-nio2/
 * - http://arquillian.org/guides/shrinkwrap_introduction/
 * - http://docs.oracle.com/javase/7/docs/technotes/guides/io/fsp/zipfilesystemprovider.html
 */
package de.weltraumschaf.juberblog.jvfs;
