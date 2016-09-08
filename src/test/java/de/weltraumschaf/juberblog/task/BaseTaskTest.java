package de.weltraumschaf.juberblog.task;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.options.Options;
import org.junit.Test;

import java.io.PrintStream;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link BaseTask}.
 *
 * @author Sven Strittmatter
 */
public class BaseTaskTest {

    private final IO io = mock(IO.class);
    private final BaseTask sut;

    public BaseTaskTest() throws ApplicationException {
        super();
        when(io.getStdout()).thenReturn(mock(PrintStream.class));
        when(io.getStderr()).thenReturn(mock(PrintStream.class));
        this.sut = new BaseTaskStub(io);
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullType() throws ApplicationException {
        new BaseTaskStub(null, io);
    }

    @Test
    public void getDesiredTypeForPreviousResult() {
        assertThat(sut.getDesiredTypeForPreviousResult().getSimpleName(), is("Void"));
    }

    private static class BaseTaskStub extends BaseTask<Void, Void> {

        public BaseTaskStub(IO io) throws ApplicationException {
            this(Void.class, io);
        }

        public BaseTaskStub(final Class<Void> typeForPreviousResult, IO io) throws ApplicationException {
            super(typeForPreviousResult, JUberblog.generateWithDefaultConfig(new Options(), io));
        }

        @Override
        public Void execute() throws Exception {
            return null;
        }

        @Override
        public Void execute(Void previousResult) throws Exception {
            return null;
        }

    }

}
