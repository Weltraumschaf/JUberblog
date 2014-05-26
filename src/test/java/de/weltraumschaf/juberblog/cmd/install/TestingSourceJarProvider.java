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

package de.weltraumschaf.juberblog.cmd.install;

import java.net.URL;

/**
 * Provides a fixture source jar.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class TestingSourceJarProvider {

    /**
     * Absolute class file name.
     */
    private static final String THIS_CLASS_FILE_NAME =
            "/de/weltraumschaf/juberblog/cmd/install/TestingSourceJarProvider.class";
    /**
     * In Maven the tests run from this directory, so we need t remove this.
     */
    private static final String TARGET_DIR_TO_REMOVE = "/target/test-classes";
    /**
     * Location of the fixture relative to the whole project.
     */
    private static final String FIXTURE_JAR =
            "/src/test/resources/de/weltraumschaf/juberblog/cmd/install/scaffold-fixture.jar";

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
