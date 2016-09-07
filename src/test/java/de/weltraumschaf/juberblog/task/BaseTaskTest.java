package de.weltraumschaf.juberblog.task;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.options.Options;
import de.weltraumschaf.juberblog.task.BaseTask;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link BaseTask}.
 *
 * @author Sven Strittmatter
 */
public class BaseTaskTest {

    private final BaseTask sut;

    public BaseTaskTest() throws ApplicationException {
        super();
        this.sut = new BaseTaskStub();
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullType() throws ApplicationException {
        new BaseTaskStub(null);
    }

    @Test
    public void getDesiredTypeForPreviousResult() {
        assertThat(sut.getDesiredTypeForPreviousResult().getSimpleName(), is("Void"));
    }

    private static class BaseTaskStub extends BaseTask<Void, Void> {

        public BaseTaskStub() throws ApplicationException {
            this(Void.class);
        }

        public BaseTaskStub(final Class<Void> typeForPreviousResult) throws ApplicationException {
            super(typeForPreviousResult, JUberblog.generateWithDefaultConfig(new Options(), mock(IO.class)));
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
