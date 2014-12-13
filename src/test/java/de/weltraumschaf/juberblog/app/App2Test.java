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
package de.weltraumschaf.juberblog.app;

import de.weltraumschaf.commons.application.IOStreams;
import de.weltraumschaf.commons.system.NullExiter;
import de.weltraumschaf.commons.testing.CapturedOutput;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for {@link App2}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class App2Test {

    @Rule
    public final CapturedOutput output = new CapturedOutput();

    private App2 createSut(final String[] args) {
        final App2 sut = new App2(args);
        sut.setExiter(new NullExiter());
        return sut;
    }

    @Test
    public void showUsageIfNoArgument() throws Exception {
        App2.main(createSut(new String[0]));

        output.expectErr("USAGE");
    }

    @Test
    public void showVersionForShortOption() throws Exception {
        App2.main(createSut(new String[] {"-v"}));

        output.expectOut("VERSION");
    }

    @Test
    public void showVersionForLongOption() throws Exception {
        App2.main(createSut(new String[] {"--version"}));

        output.expectOut("VERSION");
    }

    @Test
    public void showHelpForShortOption() throws Exception {
        App2.main(createSut(new String[] {"-h"}));

        output.expectOut("HELP");
    }

    @Test
    public void showHelpForLongOption() throws Exception {
        App2.main(createSut(new String[] {"--help"}));

        output.expectOut("HELP");
    }

}
