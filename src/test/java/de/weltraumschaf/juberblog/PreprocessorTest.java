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

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Preprocessor}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PreprocessorTest {

    private final Preprocessor sut = new Preprocessor();

    @Test
    public void process_removeBlocksAndGetData() {
        assertThat(sut.getData().size(), is(0));
        assertThat(sut.process("<?juberblog\n"
                + "  key1: value1\n"
                + "  key2: value2\n"
                + "  // comment\n"
                + "  key3: value3\n"
                + "?>\n"
                + "snafu\n"
                + "<?juberblog\n"
                + "  key4: value4\n"
                + "?>\n"
                + "foobar"), is(equalTo("snafu\nfoobar")));
//        assertThat(sut.getData().size(), is(4));
//        assertThat(sut.getData(), hasEntry("key1", "value1"));
//        assertThat(sut.getData(), hasEntry("key2", "value2"));
//        assertThat(sut.getData(), hasEntry("key3", "value3"));
//        assertThat(sut.getData(), hasEntry("key4", "value4"));
    }

}
