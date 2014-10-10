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
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Ignore;

/**
 * Tests for {@link HomeSiteGenerator}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class HomeSiteGeneratorTest {

    private final HomeSiteGenerator sut = new HomeSiteGenerator(new PublishedPages());

    @Test @Ignore
    public void execute() {
        sut.execute();
    }

}
