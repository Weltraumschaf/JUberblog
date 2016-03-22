package de.weltraumschaf.juberblog.file;

import de.weltraumschaf.commons.validate.Validate;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Finds files recursively in a given directory by file name extensions.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class FilesFinderByExtension {

    /**
     * Instance to find Markdown files.
     */
    public static final FilesFinderByExtension MARKDOWN = new FilesFinderByExtension(FileNameExtension.MARKDOWN);

    /**
     * Used to filter files.
     */
    private final FileFilter filter;

    /**
     * Dedicated constructor.
     *
     * @param type must not be {@code null}
     */
    public FilesFinderByExtension(final FileNameExtension ... type) {
        filter = new FileFilter(type);
    }

    /**
     * Find files in given directory.
     *
     * <p>
     * Throws {@link IllegalArgumentException} if given path does not exist or is not a directory.
     * </p>
     *
     * @param directory must not be {@code null}
     * @return never {@code null}, unmodifiable
     * @throws IOException if any IO error occurs
     */
    public Collection<DataFile> find(final Path directory) throws IOException {
        Validate.notNull(directory, "directory");

        if (!Files.exists(directory)) {
            throw new IllegalArgumentException(String.format("Given path '%s' does not exist!", directory));
        }

        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(String.format("Given path '%s' is not a directory!", directory));
        }

        final List<DataFile> foundFiles = new ArrayList<>();

        try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory, filter)) {
            for (final Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    foundFiles.addAll(find(path));
                } else {
                    foundFiles.add(new DataFile(path.toString()));
                }
            }
        }

        return Collections.unmodifiableList(foundFiles);
    }

    /**
     * Filters out files not ending with one of the given extensions.
     * <p>
     * Must not be used outside of the outer class.
     * </p>
     */
    private static final class FileFilter implements DirectoryStream.Filter<Path> {

        /**
         * Accepted file name extensions.
         */
        private final List<FileNameExtension> extensions;

        /**
         * Dedicated constructor.
         *
         * @param type must not be {@code null}
         */
        private FileFilter(final FileNameExtension ... type) {
            super();
            this.extensions = Arrays.asList(Validate.notNull(type, "type"));
        }

        @Override
        public boolean accept(final Path file) throws IOException {
            if (Files.isDirectory(file)) {
                return true;
            }

            for (final FileNameExtension extension : extensions) {
                if (file.toString().endsWith(extension.getExtension())) {
                    return true;
                }
            }

            return false;
        }
    }
}
