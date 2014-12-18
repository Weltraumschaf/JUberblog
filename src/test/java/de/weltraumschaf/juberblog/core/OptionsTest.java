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

package de.weltraumschaf.juberblog.core;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link Options}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class OptionsTest {

    private final Options sut = new Options();

    @Test
    public void defaults() {
        assertThat(sut.getConfigurationFile(), is(""));
        assertThat(sut.isHelp(), is(false));
        assertThat(sut.getLocation(), is(""));
        assertThat(sut.isVersion(), is(false));
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Options.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}
