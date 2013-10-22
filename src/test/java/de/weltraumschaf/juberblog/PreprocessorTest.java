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
                + "  key2: \n"
                + "  // comment\n"
                + "  key3: value3\n"
                + "?>\n"
                + "snafu\n"
                + "<?juberblog\n"
                + "  key4: value4\n"
                + "?>\n"
                + "foobar"), is(equalTo("snafu\nfoobar")));
        assertThat(sut.getData().size(), is(4));
        assertThat(sut.getData(), hasEntry("key1", "value1"));
        assertThat(sut.getData(), hasEntry("key2", ""));
        assertThat(sut.getData(), hasEntry("key3", "value3"));
        assertThat(sut.getData(), hasEntry("key4", "value4"));
    }

    @Test
    public void getMetaData() {
        sut.process("<?juberblog\n"
                + "    Navi: Projects\n"
                + "    Description: My personal projects I'm working on\n"
                + "    Keywords: Projects, Jenkins, Darcs\n"
                + "?>");
        final MetaData data = sut.getMetaData();
        assertThat(data.getNavi(), is(equalTo("Projects")));
        assertThat(data.getDescription(), is(equalTo("My personal projects I'm working on")));
        assertThat(data.getKeywords(), is(equalTo("Projects, Jenkins, Darcs")));
    }

    @Test
    public void recognizedData_ignoreMissingKey() {
        sut.process("<?juberblog\n"
                + "    Navi: Projects\n"
                + ": My personal projects I'm working on\n"
                + "    Keywords: Projects, Jenkins, Darcs\n"
                + "?>");
        assertThat(sut.getData().size(), is(2));
        assertThat(sut.getData(), hasEntry("Navi", "Projects"));
        assertThat(sut.getData(), hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void recognizedData_ignoreEmptyKey() {
        sut.process("<?juberblog\n"
                + "    Navi: Projects\n"
                + "        : My personal projects I'm working on\n"
                + "    Keywords: Projects, Jenkins, Darcs\n"
                + "?>");
        assertThat(sut.getData().size(), is(2));
        assertThat(sut.getData(), hasEntry("Navi", "Projects"));
        assertThat(sut.getData(), hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void recognizedData_ignoreMissingValue() {
        sut.process("<?juberblog\n"
                + "    Navi: Projects\n"
                + "    Description\n"
                + "    Keywords: Projects, Jenkins, Darcs\n"
                + "?>");
        assertThat(sut.getData().size(), is(2));
        assertThat(sut.getData(), hasEntry("Navi", "Projects"));
        assertThat(sut.getData(), hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void recognizedData_emptyLine() {
        sut.process("<?juberblog\n"
                + "    Navi: Projects\n"
                + "    \n"
                + "    Keywords: Projects, Jenkins, Darcs\n"
                + "?>");
        assertThat(sut.getData().size(), is(2));
        assertThat(sut.getData(), hasEntry("Navi", "Projects"));
        assertThat(sut.getData(), hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }
}
