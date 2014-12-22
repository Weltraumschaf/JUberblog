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

package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.app.Options;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@lnk JUberblog}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class JUberblogTest {

    @Test
    public void generate() {
        final Options options = new Options("./location", "config.properties");
        final IO io = mock(IO.class);

        final JUberblog product = JUberblog.generate(options, io);

        assertThat(product.options(), is(options));
        assertThat(product.io(), is(io));
    }

}
