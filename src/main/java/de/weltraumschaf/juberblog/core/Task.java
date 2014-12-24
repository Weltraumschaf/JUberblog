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

/**
 * A code unit which does something.
 *
 * @param <R> type of return value
 * @param <P> type of previous result
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Task<R, P> {

    /**
     * Executes the task.
     *
     * @return may return {@code null}
     * @throws Exception if any error happens
     */
    R execute() throws Exception;

    /**
     * Executes the task and takes the result of the previously executed task.
     *
     * @param previusResult may be {@code null}
     * @return may return {@code null}
     * @throws Exception if any error happens
     */
    R execute(P previusResult) throws Exception;

    /**
     * Get the desired type of consumed previous result (token type pattern).
     *
     * @return never {@code null}
     */
    Class<P> getDesiredTypeForPreviusResult();

}
