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
package de.weltraumschaf.juberblog.file;

import de.weltraumschaf.commons.validate.Validate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class DataFile {

    private final String absoluteFileName;

    public DataFile(final String absoluteFileName) {
        super();
        this.absoluteFileName = Validate.notEmpty(absoluteFileName, "absoluteFileName");
    }

    public Path getPath() {
        return Paths.get(absoluteFileName);
    }

    public String getBareName() {
        final int firstDashPosition = absoluteFileName.lastIndexOf("_");
        final int lastDotPosition = absoluteFileName.lastIndexOf(".");
        return absoluteFileName.substring(firstDashPosition + 1, lastDotPosition);
    }

    public String readContent(final String encoding) throws IOException {
        return new String(Files.readAllBytes(getPath()), encoding);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.absoluteFileName);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof DataFile)) {
            return false;
        }

        final DataFile other = (DataFile) obj;

        return Objects.equals(this.absoluteFileName, other.absoluteFileName);
    }

    @Override
    public String toString() {
        return "DataFile{" + "absoluteFileName=" + absoluteFileName + '}';
    }

}
