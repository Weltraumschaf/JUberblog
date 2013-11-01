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
import de.weltraumschaf.juberblog.BlogConfiguration;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.Directories;
import de.weltraumschaf.juberblog.template.Configurations;
import freemarker.template.Configuration;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublisherTest {

    /**
     * Freemarker templates resource directory.
     */
    private static final String TEMPLATE_DIRECTORRY = Constants.PACKAGE_BASE.toString() + "/template";

    @Test
    public void execute_default() throws ApplicationException, IOException, URISyntaxException {
        final Directories dirs = new Directories("", "", "");
        final Configuration templateConfig = Configurations.forTests(TEMPLATE_DIRECTORRY);
        final Publisher sut = new Publisher(dirs, templateConfig, "http://www.foobar.com/");
        sut.execute();
    }

}
