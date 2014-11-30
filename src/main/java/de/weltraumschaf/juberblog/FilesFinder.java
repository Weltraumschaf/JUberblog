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

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class FilesFinder {

    private final FileFilter filter;

    FilesFinder(final FileNameExtension type) {
        filter = new FileFilter(type);
    }

    Collection<DataFile> find(final Path directory) throws IOException {
        final List<DataFile> foundFiles = new ArrayList<>();

        try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory, filter)) {
            for (final Path path : directoryStream) {
                foundFiles.add(new DataFile(path.toString()));
            }
        }

        return Collections.unmodifiableList(foundFiles);
    }

    private static final class FileFilter implements DirectoryStream.Filter<Path> {

        private final FileNameExtension type;

        FileFilter(final FileNameExtension type) {
            super();
            this.type = type;
        }

        @Override
        public boolean accept(final Path file) throws IOException {
            return file.toString().endsWith(type.getExtension());
        }
    }
}
