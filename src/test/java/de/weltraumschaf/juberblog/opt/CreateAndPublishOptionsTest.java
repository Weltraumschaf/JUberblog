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

package de.weltraumschaf.juberblog.opt;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link CreateAndPublishOptions}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CreateAndPublishOptionsTest extends CommonOptionsTest {

    public CreateAndPublishOptionsTest() {
        super(new CreateAndPublishOptions(false, false, ""){ });
    }

    protected CreateAndPublishOptionsTest(CommonOptions sut) {
        super(sut);
    }

    private CreateAndPublishOptions sut() {
        return (CreateAndPublishOptions) sut;
    }

    @Test
    @Override
    public void defaultValues() {
        super.defaultValues();
        assertThat(sut().getConfigurationFile(), is(equalTo("")));
    }

    @Test
    public void configurationFileShortArgument() {
        optionsParser.parse(new String[]{"-c", "foobar"});
        assertThat(sut().getConfigurationFile(), is(equalTo("foobar")));
    }

    @Test
    public void configurationFileLongArgument() {
        optionsParser.parse(new String[]{"--config", "foobar"});
        assertThat(sut().getConfigurationFile(), is(equalTo("foobar")));
    }
}
