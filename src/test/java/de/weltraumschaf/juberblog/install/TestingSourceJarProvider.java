package de.weltraumschaf.juberblog.install;

import java.net.URL;

/**
 * Provides a fixture source jar.
 *
 * @author Sven Strittmatter
 */
final class TestingSourceJarProvider {

    /**
     * Absolute class file name.
     */
    private static final String THIS_CLASS_FILE_NAME =
            "/de/weltraumschaf/juberblog/install/TestingSourceJarProvider.class";
    /**
     * In Maven the tests run from this directory, so we need t remove this.
     */
    private static final String TARGET_DIR_TO_REMOVE = "/target/test-classes";
    /**
     * Location of the fixture relative to the whole project.
     */
    private static final String FIXTURE_JAR =
            "/src/test/resources/de/weltraumschaf/juberblog/install/scaffold-fixture.jar";

    /**
     * Hidden for pure static class.
     */
    private TestingSourceJarProvider() {
        super();
    }

    /**
     * Creates new provider.
     *
     * @return never {@code null}
     */
    static Scaffold.SourceJarProvider newProvider() {
        final URL uri = TestingSourceJarProvider.class.getResource(THIS_CLASS_FILE_NAME);
        final String baseDir = uri.toString().replace(TARGET_DIR_TO_REMOVE + THIS_CLASS_FILE_NAME, "");

        return new Scaffold.SourceJarProvider() {

            @Override
            public String getAbsolutePath() {
                return String.format("jar:%s%s!/%s", baseDir, FIXTURE_JAR, Scaffold.RESOURCE_LOCATION);
            }

        };
    }
}
