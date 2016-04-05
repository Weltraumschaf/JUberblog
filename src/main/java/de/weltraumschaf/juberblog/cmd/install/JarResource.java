package de.weltraumschaf.juberblog.cmd.install;

import de.weltraumschaf.commons.validate.Validate;

/**
 * A JAR resource consists of the absolute path of the JAR file and the location of the resource inside the JAR.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
final class JarResource {

    /**
     * Location of the JAR file.
     */
    private final String jarLocation;
    /**
     * Location of resource inside JAR file.
     */
    private final String resourceLocation;

    /**
     * Dedicated constructor.
     *
     * @param jarLocation must not be {@code null} or empty
     * @param resourceLocation must not be {@code null} or empty
     */
    private JarResource(final String jarLocation, final String resourceLocation) {
        super();
        this.jarLocation = Validate.notEmpty(jarLocation);
        this.resourceLocation = Validate.notEmpty(resourceLocation);
    }

    /**
     * Get the JAR file location.
     *
     * @return never {@code null} or empty
     */
    String getJarLocation() {
        return jarLocation;
    }

    /**
     * Get the resource location inside JAR.
     *
     * @return never {@code null} or empty
     */
    String getResourceLocation() {
        return resourceLocation;
    }

    /**
     * Create a resource from a given path.
     * <p>
     * A path should have the format: {@literal jar:file/path/to/jar/file.jar!/package/in/the/jar/File.class}.
     * </p>
     *
     * @param path must not be {@code null} or empty
     * @return never {@code null}
     */
    static JarResource newSourceJar(final String path) {
        Validate.notEmpty(path, "Parameter 'path' must not be null or empty!");

        if (!path.contains("!")) {
            throw new IllegalArgumentException("Path does not contain '!'!");
        }

        final String[] paths = path.split("!");

        if (paths.length < 2) {
            throw new IllegalArgumentException("The string after the '!' must not be null or empty!");
        }

        return new JarResource(
                Validate.notEmpty(paths[0], "The string before the '!' must not be null or empty!"),
                Validate.notEmpty(paths[1], "The string after the '!' must not be null or empty!"));
    }
}
