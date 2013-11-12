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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Represents a file's location in a file system.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class JVFSPath implements Path {

    /**
     * Directory separator.
     */
    private static final String DIR_SEP = JVFSFileSystems.DIR_SEP;
    /**
     * Parent directory.
     */
    private static final String DIR_UP = "..";
    /**
     * The directory self.
     */
    private static final String DIR_THIS = ".";

    /**
     * Internal representation.
     */
    private final String path;

    /**
     * Owning {@link ShrinkWrapFileSystem}.
     */
    private final JVFSFileSystem jvfs;

    /**
     * Dedicated constructor.
     *
     * @param path must not be {@code null}
     * @param fileSystem must not be {@code null}
     */
    JVFSPath(final String path, final JVFSFileSystem fileSystem) {
        super();
        JVFSAssertions.notNull(path, "path");
        JVFSAssertions.notNull(fileSystem, "fileSystem");
        this.path = path;
        this.jvfs = fileSystem;
    }

    /**
     * Convenience constructor to create root path.
     *
     * Initializes {@link #path} with {@link #DIR_SEP}.
     *
     * @param fileSystem must not be {@code null}
     */
    JVFSPath(final JVFSFileSystem fileSystem) {
        this(DIR_SEP, fileSystem);
    }

    @Override
    public FileSystem getFileSystem() {
        return jvfs;
    }

    @Override
    public boolean isAbsolute() {
        return this.path.startsWith(DIR_SEP);
    }

    @Override
    public Path getRoot() {
        return this.isAbsolute() ? new JVFSPath(jvfs) : null;
    }

    @Override
    public Path getFileName() {
        // Root and empty String has no file name
        if (path.length() == 0 || path.equals(DIR_SEP)) {
            return null;
        } else {
            final List<String> tokens = tokenize(this);
            // Furthest out
            final Path fileName = new JVFSPath(tokens.get(tokens.size() - 1), this.jvfs);
            return fileName;
        }
    }

    @Override
    public Path getParent() {
        final List<String> tokens = tokenize(this);
        // No parent?
        final int numTokens = tokens.size();

        if (numTokens == 0 || (numTokens == 1 && !this.isAbsolute())) {
            return null;
        }

        // Iterate over all but the last token and build a new path
        final StringBuffer sb = new StringBuffer();

        if (this.isAbsolute()) {
            sb.append(DIR_SEP);
        }

        for (int i = 0; i < numTokens - 1; i++) {
            if (i >= 1) {
                sb.append(DIR_SEP);
            }
            sb.append(tokens.get(i));
        }

        final String parentPath = sb.toString();
        return new JVFSPath(parentPath, jvfs);
    }

    @Override
    public int getNameCount() {
        String context = this.path;
        // Kill trailing slashes
        if (context.endsWith(DIR_SEP)) {
            context = context.substring(0, context.length() - 1);
        }
        // Kill preceding slashes
        if (context.startsWith(DIR_SEP)) {
            context = context.substring(1);
        }
        // Root
        if (context.length() == 0) {
            return 0;
        }
        // Else count names by using the separator
        final int pathSeparators = this.countOccurrences(context, DIR_SEP, 0);
        return pathSeparators + 1;
    }

    /**
     * Returns the number of occurrences of the specified character in the specified {@link String}, starting at the
     * specified offset.
     *
     * @param string
     * @param s
     * @param offset
     * @return
     */
    private int countOccurrences(final String string, final String s, int offset) {
        assert string != null : "String must be specified";
        return ((offset = string.indexOf(s, offset)) == -1) ? 0 : 1 + countOccurrences(string, s, offset + 1);
    }

    @Override
    public Path getName(int index) {
        // Precondition checks handled by subpath impl
        return this.subpath(0, index + 1);
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        JVFSAssertions.lessThan(beginIndex, 0, "beginIndex");
        JVFSAssertions.lessThan(endIndex, 0, "endIndex");
        JVFSAssertions.lessThanEqual(endIndex, beginIndex, "endIndex");

        final List<String> tokens = tokenize(this);
        final int tokenCount = tokens.size();
        if (beginIndex >= tokenCount) {
            throw new IllegalArgumentException("Invalid begin index " + endIndex + " for " + this.toString()
                    + "; must be between 0 and " + tokenCount + " exclusive");
        }
        if (endIndex > tokenCount) {
            throw new IllegalArgumentException("Invalid end index " + endIndex + " for " + this.toString()
                    + "; must be between 0 and " + tokenCount + " inclusive");
        }
        final StringBuilder newPathBuilder = new StringBuilder();
        for (int i = 0; i < endIndex; i++) {
            newPathBuilder.append(DIR_SEP);
            newPathBuilder.append(tokens.get(i));
        }
        final Path newPath = this.fromString(newPathBuilder.toString());
        return newPath;
    }

    /**
     * Creates a new {@link ShrinkWrapPath} instance from the specified input {@link String}.
     *
     * @param path must not be {@code null}
     * @return never {@code null}
     */
    private Path fromString(final String path) {
        JVFSAssertions.notNull(path, "path");
        return new JVFSPath(path, jvfs);
    }

    @Override
    public boolean startsWith(final Path other) {
        JVFSAssertions.notNull(other, "other");

        if (this.getFileSystem() != other.getFileSystem()) {
            return false;
        }

        final List<String> ourTokens = tokenize(this);
        final List<String> otherTokens = tokenize((JVFSPath) other);

        // Inequal roots
        if (other.isAbsolute() && !this.isAbsolute()) {
            return false;
        }

        // More names in the other Path than we have
        final int otherTokensSize = otherTokens.size();
        if (otherTokensSize > ourTokens.size()) {
            return false;
        }

        // Ensure each of the other name elements match ours
        for (int i = 0; i < otherTokensSize; i++) {
            if (!otherTokens.get(i).equals(ourTokens.get(i))) {
                return false;
            }
        }

        // All conditions met
        return true;
    }

    @Override
    public boolean startsWith(final String other) {
        // Precondition checks
        JVFSAssertions.notNull(other, "other");
        return this.startsWith(this.fromString(other));
    }

    @Override
    public boolean endsWith(final Path other) {
        // Precondition checks
        JVFSAssertions.notNull(other, "other");

        // Unequal FS? (also ensures that we can safely cast to this type later)
        if (this.getFileSystem() != other.getFileSystem()) {
            return false;
        }
        final List<String> ourTokens = tokenize(this);
        final List<String> otherTokens = tokenize((JVFSPath) other);

        // Bigger than us, fails
        final int numOtherTokens = otherTokens.size();
        if (numOtherTokens > ourTokens.size()) {
            return false;
        }

        // Difference in component size
        final int differential = ourTokens.size() - numOtherTokens;
        // Given an absolute?
        if (other.isAbsolute()) {
            // We must have the same number of elements
            if (differential != 0) {
                return false;
            }
        }
        // Compare all components
        for (int i = numOtherTokens - 1; i >= 0; i--) {
            if (!ourTokens.get(i + differential).equals(otherTokens.get(i))) {
                // Any tokens don't match, punt
                return false;
            }
        }

        // All conditions met
        return true;
    }

    @Override
    public boolean endsWith(final String other) {
        JVFSAssertions.notNull(other, "other");
        return this.endsWith(this.fromString(other));
    }

    @Override
    public Path normalize() {
        final String normalizedString = normalize(tokenize(this), this.isAbsolute());
        return new JVFSPath(normalizedString, this.jvfs);
    }

    @Override
    public Path resolve(final Path other) {
        JVFSAssertions.notNull(other, "other");

        if (other.isAbsolute()) {
            return other;
        }

        if (other.toString().length() == 0) {
            return this;
        }

        // Else join other to this
        final StringBuilder sb = new StringBuilder(this.path);
        if (!this.path.endsWith(DIR_SEP)) {
            sb.append(DIR_SEP);
        }
        sb.append(other.toString());

        return new JVFSPath(sb.toString(), this.jvfs);
    }

    @Override
    public Path resolve(final String other) {
        // Delegate
        return this.resolve(this.fromString(other));
    }

    @Override
    public Path resolveSibling(final Path other) {
        JVFSAssertions.notNull(other, "other");
        Path parent = getParent();
        return (parent == null) ? other : parent.resolve(other);
    }

    @Override
    public Path resolveSibling(final String other) {
        // Delegate
        return this.resolveSibling(this.fromString(other));
    }

    @Override
    public Path relativize(final Path other) {
        JVFSAssertions.notNull(other, "other");
        if (!(other instanceof JVFSPath)) {
            throw new IllegalArgumentException("Can only relativize paths of type "
                    + JVFSPath.class.getSimpleName());
        }

        // Equal paths, return empty Path
        if (this.equals(other)) {
            return new JVFSPath("", this.jvfs);
        }

        // Recursive relativization
        return relativizeCommonRoot(this, this, other, other, 0);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.nio.file.Path#register(java.nio.file.WatchService, java.nio.file.WatchEvent.Kind<?>[],
     * java.nio.file.WatchEvent.Modifier[])
     */
    @Override
    public WatchKey register(
            final WatchService watcher,
            final WatchEvent.Kind<?>[] events,
            final WatchEvent.Modifier... modifiers) throws IOException {
        throw new UnsupportedOperationException("JVFS Paths do not support registration with a watch service!");
    }

    /**
     * {@inheritDoc}
     *
     * @see java.nio.file.Path#register(java.nio.file.WatchService, java.nio.file.WatchEvent.Kind<?>[])
     */
    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) throws IOException {
        return this.register(watcher, events, (WatchEvent.Modifier) null);
    }

    @Override
    public URI toUri() {
        final URI root = JVFSFileSystems.getRootUri();
        // Compose a new URI location, stripping out the extra "/" root
        final String location = root.toString() + this.toString().substring(1);
        return URI.create(location);
    }

    @Override
    public Path toAbsolutePath() {
        // Already absolute?
        if (this.isAbsolute()) {
            return this;
        }

        // Else construct a new absolute path and normalize it
        final Path absolutePath = new JVFSPath(DIR_SEP + this.path, this.jvfs);
        return absolutePath.normalize();
    }

    @Override
    public Path toRealPath(final LinkOption... options) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File toFile() {
        // XXX Consider returning own implementation, because File will may be circumvent JVFS.
        return new File(toString());
    }

    @Override
    public Iterator<Path> iterator() {
        return new Iterator<Path>() {
            private int index;

            @Override
            public boolean hasNext() {
                return (index < getNameCount());
            }

            @Override
            public Path next() {
                if (index < getNameCount()) {
                    final Path result = getName(index);
                    index++;
                    return result;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * {@inheritDoc}
     *
     * @see java.nio.file.Path#compareTo(java.nio.file.Path)
     */
    @Override
    public int compareTo(final Path other) {
        JVFSAssertions.notNull(other, "other");
        return this.toString().compareTo(other.toString());
    }

    /**
     * {@inheritDoc}
     *
     * @see java.nio.file.Path#toString()
     */
    @Override
    public String toString() {
        return this.path;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{jvfs.hashCode(), path.hashCode()});
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JVFSPath)) {
            return false;
        }

        final JVFSPath other = (JVFSPath) obj;

        if (this.jvfs != other.jvfs) {
            return false;
        }

        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }

        return true;
    }

    /**
     * Returns the components of this path in order from root out.
     *
     * @param path must not be {@code null}
     * @return never {@code null} may be empty collection
     */
    static List<String> tokenize(final JVFSPath path) {
        return tokenize(path.toString());
    }

    static List<String> tokenize(final String path) {
        final StringTokenizer tokenizer = new StringTokenizer(path, DIR_SEP);
        final List<String> tokens = JVFSCollections.newArrayList();

        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());
        }

        return tokens;
    }

    /**
     * Normalizes the tokenized view of the path.
     *
     * @param path
     * @return
     */
    private static String normalize(final List<String> path, boolean absolute) {
        assert path != null : "path must be specified";

        // Remove unnecessary references to this dir
        if (path.contains(DIR_THIS)) {
            path.remove(DIR_THIS);
            normalize(path, absolute);
        }

        // Remove unnecessary references to the back dir, and its parent
        final int indexDirBack = path.indexOf(DIR_UP);

        if (indexDirBack != -1) {
            if (indexDirBack > 0) {
                path.remove(indexDirBack);
                path.remove(indexDirBack - 1);
                normalize(path, absolute);
            } else {
                throw new IllegalArgumentException("Cannot specify to go back \"../\" past the root");
            }
        }

        // Nothing left to do; reconstruct
        final StringBuilder sb = new StringBuilder();

        if (absolute) {
            sb.append(DIR_SEP);
        }

        for (int i = 0; i < path.size(); i++) {
            if (i > 0) {
                sb.append(DIR_SEP);
            }
            sb.append(path.get(i));
        }

        return sb.toString();
    }

    /**
     * Relativizes the paths recursively
     *
     * @param thisOriginal
     * @param thisCurrent
     * @param otherOriginal
     * @param otherCurrent
     * @param backupCount
     * @return
     */
    private static JVFSPath relativizeCommonRoot(final JVFSPath thisOriginal, final Path thisCurrent,
            final Path otherOriginal, Path otherCurrent, final int backupCount) {
        // Preconditions
        assert thisOriginal != null;
        assert thisCurrent != null;
        assert otherOriginal != null;
        assert otherCurrent != null;
        assert backupCount >= 0;

        // Do we yet have a common root?
        if (!otherCurrent.startsWith(thisCurrent)) {
            // Back up until we do
            final Path otherParent = otherCurrent.getParent();
            final JVFSPath thisParent = (JVFSPath) thisCurrent.getParent();

            if (otherParent != null && thisParent != null) {
                return relativizeCommonRoot(thisOriginal, thisParent, otherOriginal, otherParent, backupCount + 1);
            } else {
                throw new IllegalArgumentException("No common components");
            }
        }

        // Common root. Now relativize that.
        final List<String> thisTokens = tokenize(thisOriginal);
        final List<String> otherTokens = tokenize((JVFSPath) otherOriginal);
        final int numOtherTokens = otherTokens.size();
        final int numToTake = otherTokens.size() - thisTokens.size();
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < backupCount; i++) {
            sb.append(DIR_UP);
            sb.append(DIR_SEP);
        }

        final int startCounter = numOtherTokens - numToTake - backupCount;
        final int stopCounter = numOtherTokens - 1;

        for (int i = startCounter; i <= stopCounter; i++) {
            if (i > startCounter) {
                sb.append(DIR_SEP);
            }
            sb.append(otherTokens.get(i));
        }

        return new JVFSPath(sb.toString(), thisOriginal.jvfs);
    }

    FileChannel newFileChannel(final Set<? extends OpenOption> options, final FileAttribute<?>... attrs) throws IOException {
        return jvfs.newFileChannel(path, options, attrs);
    }

    SeekableByteChannel newByteChannel(final Set<? extends OpenOption> options, final FileAttribute<?>... attrs) {
        return jvfs.newByteChannel(path, options, attrs);
    }

    DirectoryStream<Path> newDirectoryStream(final DirectoryStream.Filter<? super Path> filter) {
        return new JvfsDirectoryStream(this, filter);
    }

    void createDirectory(final FileAttribute<?>... attrs) throws IOException {
        jvfs.createDirectory(path, attrs);
    }

    void delete() throws IOException {
        jvfs.delete(path);
    }

    void copy(final JVFSPath target, final CopyOption... options) throws IOException {
        jvfs.copy(path, target.toString(), options);
    }

    void move(final JVFSPath target, final CopyOption... options) throws IOException {
        jvfs.move(path, target.toString(), options);
    }

    boolean isSameFile(final Path other) {
        return equals(other);
    }

    boolean isHidden() throws IOException {
        return jvfs.isHidden(path);
    }

    FileStore getFileStore() {
        return jvfs.getFileStore();
    }

    void checkAccess(final AccessMode... modes) throws IOException {
        jvfs.checkAccess(path, modes);
    }

    void setTimes(final FileTime mtime, final FileTime atime, final FileTime ctime) throws IOException {
        jvfs.setTimes(path, mtime, atime, ctime);
    }

    JVFSFileAttributes getAttributes() throws IOException {
        final JVFSFileAttributes attrs = jvfs.getFileAttributes(path);

        if (attrs == null) {
            throw new NoSuchFileException(path);
        }

        return attrs;
    }

    Map<String, Object> readAttributes(final String attributes, final LinkOption[] options) {
        final JVFSFileAttributeView view = new JVFSFileAttributeView(this);
        return view.readAttributes(attributes);
    }

    void setAttribute(final String attribute, final Object value, final LinkOption[] options) {
        final JVFSFileAttributeView view = new JVFSFileAttributeView(this);
        view.setAttribute(attribute, value);
    }
}
