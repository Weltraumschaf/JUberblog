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
package de.weltraumschaf.juberblog.jvfs;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class JVFSPathMatcher implements PathMatcher {

    private static final String GLOB_SYNTAX = "glob";
    private static final String REGEX_SYNTAX = "regex";
    private static final String GLOB_META_CHARACTERS = "\\*?[{";
    private static final String REGEX_META_CHARACTERS = ".^$+{[]|()";
    private static char EOL = 0;

    private final Pattern pattern;

    private JVFSPathMatcher(final String expr) {
        this.pattern = Pattern.compile(expr);
    }

    @Override
    public boolean matches(final Path path) {
        return pattern.matcher(path.toString()).matches();
    }

    static PathMatcher newMatcher(final String syntaxAndPattern) {
        final int pos = syntaxAndPattern.indexOf(':');

        if (pos <= 0 || pos == syntaxAndPattern.length()) {
            throw new IllegalArgumentException(syntaxAndPattern);
        }

        final String syntax = syntaxAndPattern.substring(0, pos);
        final String input = syntaxAndPattern.substring(pos + 1);
        final String expr;

        if (syntax.equals(GLOB_SYNTAX)) {
            expr = toRegexPattern(input);
        } else {
            if (syntax.equals(REGEX_SYNTAX)) {
                expr = input;
            } else {
                throw new UnsupportedOperationException("Syntax '" + syntax
                        + "' not recognized");
            }
        }

        return new JVFSPathMatcher(expr);
    }

    private static boolean isGlobMeta(char c) {
        return GLOB_META_CHARACTERS.indexOf(c) != -1;
    }

    private static boolean isRegexMeta(char c) {
        return REGEX_META_CHARACTERS.indexOf(c) != -1;
    }

    private static char next(String glob, int i) {
        if (i < glob.length()) {
            return glob.charAt(i);
        }

        return EOL;
    }

    /**
     * Creates a regex pattern from the given glob expression.
     *
     * @param globPattern must not be {@code null}
     * @return never {@code null}
     */
    private static String toRegexPattern(final String globPattern) {
        boolean inGroup = false;
        final StringBuilder regex = new StringBuilder("^");

        int i = 0;
        while (i < globPattern.length()) {
            char c = globPattern.charAt(i++);

            switch (c) {
                case '\\':
                    // escape special characters
                    if (i == globPattern.length()) {
                        throw new PatternSyntaxException("No character to escape",
                                globPattern, i - 1);
                    }

                    final char next = globPattern.charAt(i++);

                    if (isGlobMeta(next) || isRegexMeta(next)) {
                        regex.append('\\');
                    }

                    regex.append(next);
                    break;
                case '/':
                    regex.append(c);
                    break;
                case '[':
                    // don't match name separator in class
                    regex.append("[[^/]&&[");

                    if (next(globPattern, i) == '^') {
                        // escape the regex negation char if it appears
                        regex.append("\\^");
                        i++;
                    } else {
                        // negation
                        if (next(globPattern, i) == '!') {
                            regex.append('^');
                            i++;
                        }

                        // hyphen allowed at start
                        if (next(globPattern, i) == '-') {
                            regex.append('-');
                            i++;
                        }
                    }

                    boolean hasRangeStart = false;
                    char last = 0;

                    while (i < globPattern.length()) {
                        c = globPattern.charAt(i++);
                        if (c == ']') {
                            break;
                        }

                        if (c == '/') {
                            throw new PatternSyntaxException("Explicit 'name separator' in class",
                                    globPattern, i - 1);
                        }

                        // TBD: how to specify ']' in a class?
                        if (c == '\\' || c == '['
                                || c == '&' && next(globPattern, i) == '&') {
                            // escape '\', '[' or "&&" for regex class
                            regex.append('\\');
                        }

                        regex.append(c);

                        if (c == '-') {
                            if (!hasRangeStart) {
                                throw new PatternSyntaxException("Invalid range",
                                        globPattern, i - 1);
                            }

                            if ((c = next(globPattern, i++)) == EOL || c == ']') {
                                break;
                            }

                            if (c < last) {
                                throw new PatternSyntaxException("Invalid range", globPattern, i - 3);
                            }

                            regex.append(c);
                            hasRangeStart = false;
                        } else {
                            hasRangeStart = true;
                            last = c;
                        }
                    }

                    if (c != ']') {
                        throw new PatternSyntaxException("Missing ']", globPattern, i - 1);
                    }

                    regex.append("]]");
                    break;
                case '{':
                    if (inGroup) {
                        throw new PatternSyntaxException("Cannot nest groups",
                                globPattern, i - 1);
                    }

                    regex.append("(?:(?:");
                    inGroup = true;
                    break;
                case '}':
                    if (inGroup) {
                        regex.append("))");
                        inGroup = false;
                    } else {
                        regex.append('}');
                    }

                    break;
                case ',':
                    if (inGroup) {
                        regex.append(")|(?:");
                    } else {
                        regex.append(',');
                    }

                    break;
                case '*':
                    if (next(globPattern, i) == '*') {
                        // crosses directory boundaries
                        regex.append(".*");
                        i++;
                    } else {
                        // within directory boundary
                        regex.append("[^/]*");
                    }

                    break;
                case '?':
                    regex.append("[^/]");
                    break;
                default:
                    if (isRegexMeta(c)) {
                        regex.append('\\');
                    }

                    regex.append(c);
            }
        }

        if (inGroup) {
            throw new PatternSyntaxException("Missing '}", globPattern, i - 1);
        }

        return regex.append('$').toString();
    }
}
