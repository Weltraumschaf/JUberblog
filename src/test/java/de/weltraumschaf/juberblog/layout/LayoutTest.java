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

package de.weltraumschaf.juberblog.layout;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class LayoutTest {

    @Test public void renderLAyout() {
        final Layout layout = new Layout("layout.fmt");
        assertThat(layout.render("content"), is(equalTo("")));
    }

    public class Layout {
        private final String templateFilename;

        public Layout(final String templateFilename) {
            super();
            this.templateFilename = templateFilename;
        }

        public String render(final String content) {
            return "";
        }

    }
}
