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
package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.ApplicationException;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.Directories;
import de.weltraumschaf.juberblog.model.DataFile;
import de.weltraumschaf.juberblog.template.Configurations;
import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublisherTest {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();
    private static final String TEMPLATE_DIRECTORRY = Constants.PACKAGE_BASE.toString() + "/template";
    private static final String FIXTURES_DIRECTORY = Constants.PACKAGE_BASE.toString() + "/cmd/publish";
    private File data;
    private Directories dirs;
    private Configuration templateConfig;
    private Publisher sut;

    @Before
    public void prepareFixtures() throws URISyntaxException, IOException {
        data = new File(getClass().getResource(FIXTURES_DIRECTORY).toURI());
        dirs = new Directories(
                data.getAbsolutePath(),
                "",
                tmp.getRoot().getAbsolutePath());
        templateConfig = Configurations.forTests(TEMPLATE_DIRECTORRY);
        sut = new Publisher(dirs, templateConfig, "http://www.foobar.com/");
    }
    @Test
    public void readData() {
        sut.readData();
        assertThat(sut.getPostsData(), containsInAnyOrder(
                new DataFile(data.getAbsolutePath() + "/posts/1383315520.This-is-the-First-Post.md"),
                new DataFile(data.getAbsolutePath() + "/posts/1383315841.Second-Post-About-Lorem.md")
        ));
        assertThat(sut.getSitesData(), containsInAnyOrder(
                new DataFile(data.getAbsolutePath() + "/sites/1383333526.About-me.md"),
                new DataFile(data.getAbsolutePath() + "/sites/1383333707.Projects.md")
        ));
    }

    @Test @Ignore
    public void execute_default_noSitesNoDraftsNoPurge() throws ApplicationException {
        sut.execute();
    }

}
