package de.weltraumschaf.juberblog.cmd;

import de.weltraumschaf.juberblog.cmd.SubCommand.Name;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link Name}.
 * @author Sven Strittmatter
 */
public class SubCommand_NameTest {

    @Test
    public void betterValueOf() {
        assertThat(Name.betterValueOf("CREATE"), is(Name.CREATE));
        assertThat(Name.betterValueOf("create"), is(Name.CREATE));
        assertThat(Name.betterValueOf("CreATe"), is(Name.CREATE));

        assertThat(Name.betterValueOf("INSTALL"), is(Name.INSTALL));
        assertThat(Name.betterValueOf("install"), is(Name.INSTALL));
        assertThat(Name.betterValueOf("iNStall"), is(Name.INSTALL));

        assertThat(Name.betterValueOf("PUBLISH"), is(Name.PUBLISH));
        assertThat(Name.betterValueOf("publish"), is(Name.PUBLISH));
        assertThat(Name.betterValueOf("pUblISh"), is(Name.PUBLISH));

        assertThat(Name.betterValueOf("foobar"), is(Name.UNKNOWN));
    }

    @Test
    public void isSubCommand() {
        assertThat(Name.isSubCommand("CREATE"), is(true));
        assertThat(Name.isSubCommand("create"), is(true));
        assertThat(Name.isSubCommand("CreATe"), is(true));

        assertThat(Name.isSubCommand("INSTALL"), is(true));
        assertThat(Name.isSubCommand("install"), is(true));
        assertThat(Name.isSubCommand("iNStall"), is(true));

        assertThat(Name.isSubCommand("PUBLISH"), is(true));
        assertThat(Name.isSubCommand("publish"), is(true));
        assertThat(Name.isSubCommand("pUblISh"), is(true));

        assertThat(Name.isSubCommand("foobar"), is(false));
    }
}
