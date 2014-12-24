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

import com.beust.jcommander.internal.Lists;
import de.weltraumschaf.commons.validate.Validate;
import java.util.Collection;

/**
 * Executes the given tasks in same order as they were appended.
 * <p>
 * Also the executor collects the result from an executed task and passes it
 * in to the next task, unless the result is {@code null}.
 * </p>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class TaskExecutor {

    /**
     * Collects the tasks to be executed.
     */
    private final Collection<Task<?, ?>> tasks = Lists.newLinkedList();

    /**
     * Appends a task for execution.
     *
     * @param task must not be {@code null}
     * @return self for method chaining
     */
    public TaskExecutor append(final Task task) {
        tasks.add(Validate.notNull(task, "task"));
        return this;
    }

    /**
     * Executes all appended tasks.
     *
     * @throws Exception if a task throws an exception, execution will be interrupted
     */
    public void execute() throws Exception {
        Object result = null;

        for (final Task task : tasks) {
            result = executeTask(task, result);
        }
    }

    /**
     * Executes a single task with optional result from previous task.
     *
     * @param task must not be {@code null}
     * @param result may be {@code null}
     * @return may return {@code null}
     * @throws Exception if the executed task produces an error
     */
    private Object executeTask(final Task task, final Object result) throws Exception {
        if (null != result && hasResultExpectedType(task, result)) {
            return executeUnchecked(task, result);
        } else {
            return task.execute();
        }
    }

    /**
     * Determines if the given result will be consumed by the given task.
     *
     * @param task must not be {@code null}
     * @param result must not be {@code null}
     * @return {@code true} if task will accepts result, else {@code false}
     */
    private static boolean hasResultExpectedType(final Task task, final Object result) {
        return Validate.notNull(result, "result")
                .getClass()
                .equals(
                        Validate.notNull(task, "task").getDesiredTypeForPreviusResult());
    }

    /**
     * Used to encapsulate unchecked code in smallest possible scope.
     *
     * @param task must not be {@code null}
     * @param result must not be {@code null}
     * @return may be {@code null}
     * @throws Exception if the executed task produces an error
     */
    @SuppressWarnings("unchecked")
    private Object executeUnchecked(final Task task, final Object result) throws Exception {
        return Validate.notNull(task, "task").execute(Validate.notNull(result, "result"));
    }

}
