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
package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.juberblog.core.TaskExecutor;
import de.weltraumschaf.juberblog.core.Task;
import org.junit.Test;
import org.mockito.InOrder;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

/**
 * Tests for {@link TaskExecutor}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class TaskExecutorTest {

    private final TaskExecutor sut = new TaskExecutor();

    @Test(expected = NullPointerException.class)
    public void append_doesNotAllowNull() {
        sut.append(null);
    }

    @Test
    public void execute_noTasks() throws Exception {
        // We expect no error here.
        sut.execute();
    }

    @Test
    public void execute_twoTasksInOrder() throws Exception {
        final Task taskOne = mock(Task.class);
        sut.append(taskOne);
        final Task taskTwo = mock(Task.class);
        sut.append(taskTwo);

        sut.execute();

        final InOrder inOrder = inOrder(taskOne, taskTwo);
        inOrder.verify(taskOne, times(1)).execute();
        inOrder.verify(taskTwo, times(1)).execute();
    }

    @Test
    public void execute_previousResultIsPassedInToNextTask() throws Exception {
        final FirstTask first = spy(new FirstTask());
        sut.append(first);
        final SecondTask second = spy(new SecondTask());
        sut.append(second);
        final ThirdTask third = spy(new ThirdTask());
        sut.append(third);

        sut.execute();

        final InOrder inOrder = inOrder(first, second, third);
        inOrder.verify(first, times(1)).execute();
        inOrder.verify(second, times(1)).execute("FirstTask");
        inOrder.verify(third, times(1)).execute("SecondTask");
    }

    private static class FirstTask implements Task<String, Void> {

        @Override
        public String execute() throws Exception {
            return "FirstTask";
        }

        @Override
        public String execute(final Void previusResult) throws Exception {
            return execute();
        }

        @Override
        public Class<Void> getDesiredTypeForPreviusResult() {
            return Void.class;
        }
    }

    private static class SecondTask implements Task<String, String> {

        @Override
        public String execute() throws Exception {
            return execute("");
        }

        @Override
        public String execute(final String previusResult) throws Exception {
            return "SecondTask";
        }

        @Override
        public Class<String> getDesiredTypeForPreviusResult() {
            return String.class;
        }
    }

    private static class ThirdTask implements Task<Void, String> {

        @Override
        public Void execute() throws Exception {
            return execute("");
        }

        @Override
        public Void execute(final String previusResult) throws Exception {
            return null;
        }

        @Override
        public Class<String> getDesiredTypeForPreviusResult() {
            return String.class;
        }
    }
}
