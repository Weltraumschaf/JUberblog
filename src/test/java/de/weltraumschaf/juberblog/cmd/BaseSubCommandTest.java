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

package de.weltraumschaf.juberblog.cmd;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.opt.Options;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link BaseSubCommand}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class BaseSubCommandTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    @Test
    public void throwsExceptionIfConstructedWithNull() {
        thrown.expect(NullPointerException.class);
        new BaseSubCommandStub(null);
    }

    @Test
    public void execute_throwsExceptionIfOptionsIsNull() throws ApplicationException {
        thrown.expect(NullPointerException.class);
        new BaseSubCommandStub(mock(IO.class)).execute();
    }

    @Test
    public void execute() throws ApplicationException {
        final BaseSubCommand sut = spy(new BaseSubCommandStub(mock(IO.class)));
        sut.setOptions(mock(Options.class));
        sut.execute();
        verify(sut).getOptions();
        verify(sut).init();
        verify(sut).run();
        verify(sut).deinit();
    }

    /**
     * Testable implementation.
     */
    private static class BaseSubCommandStub extends BaseSubCommand {

        private Options options;

        public BaseSubCommandStub(final IO io) {
            super(io);
        }

        @Override
        protected void run() {

        }

        @Override
        public void setOptions(final Options opt) {
            this.options = opt;
        }

        @Override
        public Options getOptions() {
            return options;
        }

    }
}
