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

import de.weltraumschaf.commons.validate.Validate;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
abstract class BaseTask<R, P> implements Task<R, P> {

    private final Class<P> typeForPreviusResult;

    public BaseTask(final Class<P> typeForPreviusResult) {
        super();
        this.typeForPreviusResult = Validate.notNull(typeForPreviusResult, "typeForPreviusResult");
    }

    @Override
    public final Class<P> getDesiredTypeForPreviusResult() {
        return typeForPreviusResult;
    }

    @SuppressWarnings("unchecked")
    static <T> Class<T> castClass(Class<?> aClass) {
        return (Class<T>)aClass;
    }
}
