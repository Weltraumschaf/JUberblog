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

import de.weltraumschaf.juberblog.model.PublishedPages;
import de.weltraumschaf.juberblog.template.Configurations;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Test;
import org.junit.Ignore;

/**
 * Tests for {@link HomeSiteGenerator}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class HomeSiteGeneratorTest {

    private final HomeSiteGenerator sut = new HomeSiteGenerator(
            Configurations.forTests(Configurations.SCAFFOLD_TEMPLATE_DIR), new PublishedPages());

    public HomeSiteGeneratorTest() throws IOException, URISyntaxException {
        super();
    }

    @Test @Ignore
    public void execute() {
        sut.execute();
    }

}
