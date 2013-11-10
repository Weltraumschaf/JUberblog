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

package de.weltraumschaf.juberblog.jvfs;

import java.io.File;
import java.net.URI;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class JVFSFile extends File {

    public JVFSFile(String pathname) {
        super(pathname);
    }

    public JVFSFile(String parent, String child) {
        super(parent, child);
    }

    public JVFSFile(File parent, String child) {
        super(parent, child);
    }

    public JVFSFile(URI uri) {
        super(uri);
    }

}
