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

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.system.ExitCode;
import de.weltraumschaf.juberblog.cmd.SubCommand;
import de.weltraumschaf.juberblog.cmd.SubCommands;
import de.weltraumschaf.juberblog.opt.CreateOptions;
import de.weltraumschaf.juberblog.opt.InstallOptions;
import de.weltraumschaf.juberblog.opt.PublishOptions;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link App}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AppTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    private App createSut() {
        return createSut(new String[]{});
    }

    private App createSut(final String... args) {
        final App app = new App(args);
        app.setIoStreams(mock(IO.class));
        return app;
    }

    @Test
    public void isDebug_returnFalseByDefault() {
        assertThat(createSut().isDebug(), is(false));
    }

    @Test
    public void createSubcommand_throwsExceptionIfNull() throws ApplicationException {
        thrown.expect(NullPointerException.class);
        createSut().createSubcommand(null);
    }

    @Test
    public void createSubcommand_throwsExceptionIfUnknown() throws ApplicationException {
        try {
            createSut().createSubcommand(SubCommands.NOT_IMPLEMENTED);
            fail("Expected exception not thrown!");
        } catch (final ApplicationException ex) {
            assertThat(ex.getExitCode(), is((ExitCode) ExitCodeImpl.UNKNOWN_COMMAND));
            assertThat(ex.getMessage(), is(equalTo("Unknown command type 'not-implemented'!")));
        }
    }

    @Test
    public void createSubcommand() throws ApplicationException {
        final App sut = createSut();
        assertThat(sut.createSubcommand(SubCommands.CREATE), is(not(nullValue())));
        assertThat(sut.createSubcommand(SubCommands.INSTALL), is(not(nullValue())));
        assertThat(sut.createSubcommand(SubCommands.PUBLISH), is(not(nullValue())));
    }

    @Test
    public void validateArguments_emptyArgs() {
        final App sut = createSut();
        try {
            sut.validateArguments();
            fail("Expected exception not thrown!");
        } catch (final ApplicationException ex) {
            assertThat(ex.getExitCode(), is((ExitCode) ExitCodeImpl.TOO_FEW_ARGUMENTS));
            assertThat(ex.getMessage(), is(equalTo("No sub comamnd given!"
                    + Constants.DEFAULT_NEW_LINE
                    + "Usage: juberblog [-v|--version] [-h|--help] create|publish|install")));
        }
    }

    @Test
    public void validateArguments_firstArgIsEmpty() {
        final App sut = createSut("");

        try {
            sut.validateArguments();
            fail("Expected exception not thrown!");
        } catch (final ApplicationException ex) {
            assertThat(ex.getExitCode(), is((ExitCode) ExitCodeImpl.TOO_FEW_ARGUMENTS));
            assertThat(ex.getMessage(), is(equalTo("No sub comamnd given!"
                    + Constants.DEFAULT_NEW_LINE
                    + "Usage: juberblog [-v|--version] [-h|--help] create|publish|install")));
        }
    }

    @Test
    public void validateArguments_allArgsAreEmpty() {
        final App sut = createSut("", "", "");

        try {
            sut.validateArguments();
            fail("Expected exception not thrown!");
        } catch (final ApplicationException ex) {
            assertThat(ex.getExitCode(), is((ExitCode) ExitCodeImpl.TOO_FEW_ARGUMENTS));
            assertThat(ex.getMessage(), is(equalTo("No sub comamnd given!"
                    + Constants.DEFAULT_NEW_LINE
                    + "Usage: juberblog [-v|--version] [-h|--help] create|publish|install")));
        }
    }

    @Test
    public void validateArguments_oneArgument() throws ApplicationException {
        final App sut = createSut("foo");
        final Arguments args = sut.validateArguments();
        assertThat(args, is(not(nullValue())));
        assertThat(args.getFirstArgument(), is(equalTo("foo")));
        assertThat(args.getTailArguments(), is(new String[]{}));
    }

    @Test
    public void validateArguments_multipleArggs() throws ApplicationException {
        final App sut = createSut("foo", "bar", "baz");
        final Arguments args = sut.validateArguments();
        assertThat(args, is(not(nullValue())));
        assertThat(args.getFirstArgument(), is(equalTo("foo")));
        assertThat(args.getTailArguments(), is(new String[]{"bar", "baz"}));
    }

    @Test
    public void parseOptions_throwsExceptionIfTypeIsNull() throws ApplicationException {
        final App sut = createSut();
        thrown.expect(NullPointerException.class);
        sut.parseOptions(null, new Arguments(), mock(SubCommand.class));
    }

    @Test
    public void parseOptions_throwsExceptionIfArgsIsNull() throws ApplicationException {
        final App sut = createSut();
        thrown.expect(NullPointerException.class);
        sut.parseOptions(SubCommands.CREATE, null, mock(SubCommand.class));
    }

    @Test
    public void parseOptions_throwsExceptionIfSubCommandIsNull() throws ApplicationException {
        final App sut = createSut();
        thrown.expect(NullPointerException.class);
        sut.parseOptions(SubCommands.CREATE, new Arguments(), null);
    }

    @Test
    public void parseOptions_forCreateSubcommand() throws ApplicationException {
        final App sut = createSut();
        final SubCommands type = SubCommands.CREATE;
        final SubCommand cmd = SubCommands.create(type, mock(IO.class), new Version("."));
        sut.parseOptions(type, new Arguments(new String[]{type.toString(), "-s", "--title", "foo"}), cmd);
        final CreateOptions opt = (CreateOptions) cmd.getOptions();
        assertThat(opt, is(not(nullValue())));
        assertThat(opt.isHelp(), is(false));
        assertThat(opt.isVerbose(), is(false));
        assertThat(opt.isSite(), is(true));
        assertThat(opt.getTitle(), is(equalTo("foo")));
    }

    @Test
    public void parseOptions_forInstallSubcommand() throws ApplicationException {
        final App sut = createSut();
        final SubCommands type = SubCommands.INSTALL;
        final SubCommand cmd = SubCommands.create(type, mock(IO.class), new Version("."));
        sut.parseOptions(type, new Arguments(new String[]{type.toString(), "-l", "foo"}), cmd);
        final InstallOptions opt = (InstallOptions) cmd.getOptions();
        assertThat(opt, is(not(nullValue())));
        assertThat(opt.isHelp(), is(false));
        assertThat(opt.isVerbose(), is(false));
        assertThat(opt.getLocation(), is(equalTo("foo")));
    }

    @Test
    public void parseOptions_forPublishSubcommand() throws ApplicationException {
        final App sut = createSut();
        final SubCommands type = SubCommands.PUBLISH;
        final SubCommand cmd = SubCommands.create(type, mock(IO.class), new Version("."));
        sut.parseOptions(type, new Arguments(new String[]{type.toString(), "-p", "-q", "--sites"}), cmd);
        final PublishOptions opt = (PublishOptions) cmd.getOptions();
        assertThat(opt, is(not(nullValue())));
        assertThat(opt.isHelp(), is(false));
        assertThat(opt.isVerbose(), is(false));
        assertThat(opt.isPurge(), is(true));
        assertThat(opt.isQuiet(), is(true));
        assertThat(opt.isSites(), is(true));
    }

    @Test
    public void parseOptions_unsupportedType() throws ApplicationException {
        thrown.expect(NullPointerException.class);
        final App sut = createSut();
        sut.parseOptions(
                SubCommands.NOT_IMPLEMENTED,
                new Arguments(),
                SubCommands.create(SubCommands.CREATE, mock(IO.class), new Version(".")));
    }

    @Test
    public void parseOptions_showHelp() {
        final App sut = createSut();
        final SubCommands type = SubCommands.PUBLISH;
        final SubCommand cmd = SubCommands.create(type, mock(IO.class), new Version("."));

        try {
            sut.parseOptions(type, new Arguments(new String[]{type.toString(), "-h"}), cmd);
            fail("Expected exception not thrown!");
        } catch (final ApplicationException ex) {
            assertThat(ex.getExitCode(), is((ExitCode) ExitCodeImpl.OK));
        }
    }
}
