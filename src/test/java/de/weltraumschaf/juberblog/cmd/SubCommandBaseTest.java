package de.weltraumschaf.juberblog.cmd;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.options.Options;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.mockito.InOrder;
import static org.mockito.Mockito.*;

/**
 */
public class SubCommandBaseTest {

    private final Options opts = new Options();
    private final IO io = mock(IO.class);
    private final JUberblog registry = JUberblog.generateWithDefaultConfig(opts, io);
    private final SubCommandBase sut = new SubCommandBaseStub(registry);

    public SubCommandBaseTest() throws ApplicationException {
        super();
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructWithNullThrowsException() {
        new SubCommandBaseStub(null);
    }

    @Test
    public void options() {
        assertThat(sut.options(), is(sameInstance(opts)));
    }

    @Test
    public void io() {
        assertThat(sut.io(), is(sameInstance(io)));
    }

    @Test
    public void templates() {
        assertThat(sut.templates(), is(sameInstance(registry.templates())));
    }

    @Test
    public void directories() {
        assertThat(sut.directories(), is(sameInstance(registry.directories())));
    }

    @Test
    public void configuration() {
        assertThat(sut.configuration(), is(sameInstance(registry.configuration())));
    }

    @Test
    public void execute() throws ApplicationException {
        final SubCommandBase spied = spy(sut);

        spied.execute();

        InOrder inOrder = inOrder(spied);
        inOrder.verify(spied).validateArguments();
        inOrder.verify(spied).doExecute();
    }

    private static class SubCommandBaseStub extends SubCommandBase {

        SubCommandBaseStub(JUberblog registry) {
            super(registry);
        }

        @Override
        protected void doExecute() throws ApplicationException {
        }

    }

}
