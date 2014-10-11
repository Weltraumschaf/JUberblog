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

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.testing.CapturedOutput;
import de.weltraumschaf.juberblog.opt.InstallOptions;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link InstallSubCommand}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class InstallSubCommandTest {

    private static final Collection<String> EXPECTED_FILES = Arrays.asList(
            "configuration/configuration.sample.properties",
            "data/drafts/posts/README",
            "data/drafts/sites/README",
            "data/posts/README",
            "data/sites/README",
            "public/css/main.css",
            "public/css/README",
            "public/drafts/posts/README",
            "public/drafts/sites/README",
            "public/img/favicon.ico",
            "public/img/logo.jpg",
            "public/img/raty/background.gif",
            "public/img/raty/cancel-off-big.png",
            "public/img/raty/cancel-off.png",
            "public/img/raty/cancel-on-big.png",
            "public/img/raty/cancel-on.png",
            "public/img/raty/coffee.png",
            "public/img/raty/face-a-off.png",
            "public/img/raty/face-a.png",
            "public/img/raty/face-b-off.png",
            "public/img/raty/face-b.png",
            "public/img/raty/face-c-off.png",
            "public/img/raty/face-c.png",
            "public/img/raty/face-d-off.png",
            "public/img/raty/face-d.png",
            "public/img/raty/face-off.png",
            "public/img/raty/medal-off.png",
            "public/img/raty/medal-on.png",
            "public/img/raty/star-half-big.png",
            "public/img/raty/star-half.png",
            "public/img/raty/star-off-big.png",
            "public/img/raty/star-off.png",
            "public/img/raty/star-on-big.png",
            "public/img/raty/star-on.png",
            "public/img/rss.png",
            "public/js/handlebars.js",
            "public/js/jquery-1.7.2.js",
            "public/js/jquery-1.7.2.min.js",
            "public/js/jquery.raty.js",
            "public/js/jquery.raty.min.js",
            "public/js/LAB.js",
            "public/js/LAB.src.js",
            "public/js/main.js",
            "public/posts/README",
            "public/robots.txt",
            "public/sites/README",
            "README.md",
            "templates/create/post_or_site.md.ftl",
            "templates/feed.ftl",
            "templates/index.ftl",
            "templates/layout.ftl",
            "templates/post.ftl",
            "templates/site.ftl",
            "templates/site_map.ftl"
    );


    @Rule
    //CHECKSTYLE:OFF
    public final TemporaryFolder tmp = new TemporaryFolder();
    //CHECKSTYLE:ON

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    @Rule
    //CHECKSTYLE:OFF
    public final CapturedOutput output = new CapturedOutput();
    //CHECKSTYLE:ON

    private final IO io = mock(IO.class);
    private final InstallSubCommand sut = new InstallSubCommand(io, new Version("."));

    public InstallSubCommandTest() throws UnsupportedEncodingException {
        super();
    }

    @Test
    public void execute_withoutAnyOption() throws ApplicationException {
        sut.setOptions(new InstallOptions());

        thrown.expect(ApplicationException.class);
        thrown.expectMessage("Empty location given! Please specify a valid direcotry as installation location.");

        sut.execute();
    }

    @Test
    public void execute_withLocationAndVerboseOption() throws ApplicationException {
        final InstallOptions options = createOptionsWithDir(false, true);
        sut.setSrcJar(TestingSourceJarProvider.newProvider());
        sut.setOptions(options);

        sut.execute();
        verify(io, times(1)).println("Install scaffold to '"
                + tmp.getRoot().getAbsolutePath()
                + "'...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/README.md to "
                + tmp.getRoot().getAbsolutePath()
                + "/README.md ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/templates");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/site_map.ftl to "
                + tmp.getRoot().getAbsolutePath()
                + "/templates/site_map.ftl ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/site.ftl to "
                + tmp.getRoot().getAbsolutePath()
                + "/templates/site.ftl ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/post.ftl to "
                + tmp.getRoot().getAbsolutePath()
                + "/templates/post.ftl ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/layout.ftl to "
                + tmp.getRoot().getAbsolutePath()
                + "/templates/layout.ftl ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/index.ftl to "
                + tmp.getRoot().getAbsolutePath()
                + "/templates/index.ftl ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/feed.ftl to "
                + tmp.getRoot().getAbsolutePath()
                + "/templates/feed.ftl ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/templates/create");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/create/"
                + "post_or_site.md.ftl to "
                + tmp.getRoot().getAbsolutePath()
                + "/templates/create/post_or_site.md.ftl ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/public");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/robots.txt to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/robots.txt ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/public/sites");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/sites/README to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/sites/README ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/public/posts");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/posts/README to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/posts/README ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/public/js");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/js/main.js to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/js/main.js ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/js/LAB.src.js to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/js/LAB.src.js ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/js/LAB.js to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/js/LAB.js ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/js/jquery.raty.min.js to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/js/jquery.raty.min.js ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/js/jquery.raty.js to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/js/jquery.raty.js ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/js/jquery-1.7.2.min.js to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/js/jquery-1.7.2.min.js ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/js/jquery-1.7.2.js to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/js/jquery-1.7.2.js ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/js/handlebars.js to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/js/handlebars.js ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/rss.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/rss.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/logo.jpg to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/logo.jpg ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/favicon.ico to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/favicon.ico ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/star-on.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/star-on.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/star-on-big.png "
                + "to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/star-on-big.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/star-off.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/star-off.png ...");
        verify(io, times(1)).println(
                "Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/star-off-big.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/star-off-big.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/star-half.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/star-half.png ...");
        verify(io, times(1)).println(
                "Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/star-half-big.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/star-half-big.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/medal-on.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/medal-on.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/medal-off.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/medal-off.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/face-off.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/face-off.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/face-d.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/face-d.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/face-d-off.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/face-d-off.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/face-c.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/face-c.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/face-c-off.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/face-c-off.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/face-b.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/face-b.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/face-b-off.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/face-b-off.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/face-a.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/face-a.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/face-a-off.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/face-a-off.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/coffee.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/coffee.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/cancel-on.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/cancel-on.png ...");
        verify(io, times(1)).println(
                "Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/cancel-on-big.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/cancel-on-big.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/cancel-off.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/cancel-off.png ...");
        verify(io, times(1)).println(
                "Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/cancel-off-big.png to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/cancel-off-big.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/raty/background.gif to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/img/raty/background.gif ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/public/drafts");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/public/drafts/sites");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/drafts/sites/README to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/drafts/sites/README ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/public/drafts/posts");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/drafts/posts/README to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/drafts/posts/README ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/public/css");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/css/README to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/css/README ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/css/main.css to "
                + tmp.getRoot().getAbsolutePath()
                + "/public/css/main.css ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/data");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/data/sites");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/data/sites/README to "
                + tmp.getRoot().getAbsolutePath()
                + "/data/sites/README ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/data/posts");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/data/posts/README to "
                + tmp.getRoot().getAbsolutePath()
                + "/data/posts/README ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/data/drafts");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/data/drafts/sites");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/data/drafts/sites/README to "
                + tmp.getRoot().getAbsolutePath()
                + "/data/drafts/sites/README ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/data/drafts/posts");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/data/drafts/posts/README to "
                + tmp.getRoot().getAbsolutePath()
                + "/data/drafts/posts/README ...");
        verify(io, times(1)).println("Create directory "
                + tmp.getRoot().getAbsolutePath()
                + "/configuration");
        verify(io, times(1)).println(
                "Copy file /de/weltraumschaf/juberblog/scaffold/configuration/configuration.sample.properties to "
                + tmp.getRoot().getAbsolutePath()
                + "/configuration/configuration.sample.properties ...");

        final Collection<File> installedFiles = FileUtils.listFiles(tmp.getRoot(), null, true);
        assertThat(installedFiles.toString(), installedFiles, hasSize(EXPECTED_FILES.size()));

        for (final String path : EXPECTED_FILES) {
            assertThat(path, installedFiles.contains(new File(tmp.getRoot(), path)), is(true));
        }
    }

    @Test
    public void execute_withLocation() throws ApplicationException {
        final InstallOptions options = createOptionsWithDir(false, false);
        sut.setSrcJar(TestingSourceJarProvider.newProvider());
        sut.setOptions(options);

        sut.execute();
        verify(io, times(1)).println("Install scaffold to '" + tmp.getRoot().getAbsolutePath() + "'...");

        final Collection<File> installedFiles = FileUtils.listFiles(tmp.getRoot(), null, true);
        assertThat(installedFiles.toString(), installedFiles, hasSize(EXPECTED_FILES.size()));

        for (final String path : EXPECTED_FILES) {
            assertThat(path, installedFiles.contains(new File(tmp.getRoot(), path)), is(true));
        }
    }

    private InstallOptions createOptionsWithDir(final boolean help, final boolean verbose) {
        final String tmpDir = tmp.getRoot().getAbsolutePath();
        final InstallOptions options = new InstallOptions(help, verbose, tmpDir, false, false);
        assertThat(options.getLocation(), is(equalTo(tmpDir)));

        return options;
    }

}
