package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.guava.Maps;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.joda.time.DateTime;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link BaseTask}.
 *
 * @author Sven Strittmatter
 */
public class BaseTaskTest {

    private final BaseTask sut = new BaseTaskStub();

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullType() {
        new BaseTaskStub(null);
    }

    @Test
    public void getDesiredTypeForPreviusResult() {
        assertThat(sut.getDesiredTypeForPreviusResult().getSimpleName(), is("Void"));
    }

    private static class BaseTaskStub extends BaseTask<Void, Void> {

        public BaseTaskStub() {
            this(Void.class);
        }

        public BaseTaskStub(final Class<Void> typeForPreviusResult) {
            super(typeForPreviusResult);
        }

        @Override
        public Void execute() throws Exception {
            return null;
        }

        @Override
        public Void execute(Void previusResult) throws Exception {
            return null;
        }

    }

}
