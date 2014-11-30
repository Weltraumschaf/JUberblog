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

import com.beust.jcommander.internal.Lists;
import de.weltraumschaf.commons.validate.Validate;
import java.util.List;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class TaskExecutor {

    private final List<Task> tasks = Lists.newLinkedList();

    public TaskExecutor append(final Task task) {
        tasks.add(Validate.notNull(task, "task"));
        return this;
    }

    public void execute() throws Exception {
        for (final Task task : tasks) {
            task.execute();
        }
    }

}
