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
package de.weltraumschaf.juberblog.filter;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@code FilterChain}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FilterChainTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON
    private final FilterChain sut = new FilterChain();

    @Test
    public void apply_throwsExceptionIfNull() {
        thrown.expect(NullPointerException.class);
        sut.apply(null);
    }

    @Test
    public void apply_returnSameAsInputIfNoFilters() {
        assertThat(sut.apply(""), is(equalTo("")));
        assertThat(sut.apply("foobar"), is(equalTo("foobar")));
    }

    @Test
    public void apply_oneFilter() {
        sut.add(new FilterOne());
        assertThat(sut.apply("foobar"), is(equalTo("foobar1")));
    }

    @Test
    public void apply_fourFilters() {
        sut.add(new FilterOne());
        sut.add(new FilterTwo());
        sut.add(new FilterThree());
        sut.add(new FilterFour());
        assertThat(sut.apply("foobar"), is(equalTo("foobar1234")));
    }

    private static final class FilterOne implements Filter {

        @Override
        public String apply(final String input) {
            return input + "1";
        }
    }

    private static final class FilterTwo implements Filter {

        @Override
        public String apply(final String input) {
            return input + "2";
        }
    }

    private static final class FilterThree implements Filter {

        @Override
        public String apply(final String input) {
            return input + "3";
        }
    }

    private static final class FilterFour implements Filter {

        @Override
        public String apply(final String input) {
            return input + "4";
        }
    }
}
