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

import com.google.common.collect.Maps;
import de.weltraumschaf.commons.IOStreams;
import de.weltraumschaf.juberblog.CliOptions;
import de.weltraumschaf.juberblog.Constants;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.log4j.Logger;

/**
 * Installation command.
 *
 * Creates a scaffold directory in specified location.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class Install extends BaseCommand {

    private static final boolean REQUIRE_CONFIGURATION = false;
    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(Create.class);

    public Install(final CliOptions options, final IOStreams io) {
        super(options, io, REQUIRE_CONFIGURATION);
    }

    @Override
    protected void run() {
        LOG.debug("install");
        final String[] paths = getClass().getResource(Constants.DIR_SEP.toString()
                + Constants.SCAFFOLD_PACKAGE.toString()
                .replace(".", Constants.DIR_SEP.toString()))
                .toString()
                .split("!");
        final String jarFile = paths[0];
        LOG.debug(jarFile);

        try {
            try (FileSystem fs = FileSystems.newFileSystem(URI.create(jarFile), Maps.<String, String>newHashMap())) {
                final String scaffold = paths[1];
                LOG.debug(scaffold);
                final Path dir = fs.getPath(scaffold);
                Files.walkFileTree(dir,
                    new CopyDirVisitor(
                        new File("/Users/sxs/tmp/juberblog").toPath(),
                        "/de/weltraumschaf/juberblog/scaffold/"));
            }
        } catch (IOException ex) {
            io.errorln("Can't copy scaffold: " + ex.getMessage());
            io.printStackTrace(ex);
        }
    }

    /**
     * http://codingjunkie.net/java-7-copy-move/
     */
    private static class CopyDirVisitor extends SimpleFileVisitor<Path> {

        private final Path toPath;
        private final String prefix;
        private final StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;

        public CopyDirVisitor(final Path toPath, final String prefix) {
            super();
            this.toPath = toPath;
            this.prefix = prefix;
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
            final FileVisitResult result = super.preVisitDirectory(dir, attrs);
//            LOG.debug("Dir: " + dir);
//            final Path targetPath = toPath.resolve(fromPath.relativize(dir));
//
//            if (!Files.exists(targetPath)) {
//                Files.createDirectory(targetPath);
//            }

            return result;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            final FileVisitResult result = super.visitFile(file, attrs);
            final Path target = toPath.resolve(baseName(file));
            LOG.debug(String.format("Copy file %s to %s", file, target));
            Files.copy(file, target, copyOption);
            return result;
        }

        private String baseName(final Path file) {
            return file.toString().replace(prefix, "");
        }
    }
}
