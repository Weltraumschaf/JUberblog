package de.weltraumschaf.juberblog.app;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link Options}.
 *
 * @author Sven Strittmatter
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
