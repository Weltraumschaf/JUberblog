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
package de.weltraumschaf.juberblog.tasks;

import org.junit.Test;
import org.mockito.InOrder;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
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

}
