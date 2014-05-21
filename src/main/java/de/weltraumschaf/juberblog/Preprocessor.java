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
package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.model.MetaData;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Parses all blocks of preprocessor code.
 *
 * Preprocessor blocks are opened by {@code <?juberblog} and closed by {@code ?>}. Between these markers key value pairs
 * are recognized. The blocks are removed.
 *
 * Example:
 * <pre>
 * ...
 * &gt;?juberblog
 *   key1: value1
 *   key2: value2
 *   // ...
 *   keyN: valueN
 * ?&lt;
 * ...
 * </pre>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Preprocessor {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(Preprocessor.class);
    /**
     * Indicates beginning of preprocessor area.
     */
    private static final String START_TOKEN = "<?juberblog";
    /**
     * Indicates end of preprocessor area.
     */
    private static final String END_TOKEN = "?>";
    /**
     * Separates key value pair.
     *
     * Example: {@code key ":" value NL}
     */
    private static final String SPLIT_TOKEN = ":";
    /**
     * Holds recognized data.
     */
    private final Map<String, String> data = Maps.newHashMap();

    /**
     * Processes the input.
     *
     * Each invocation clears the {@link #getData() recognized data}.
     *
     * @param in must not be {@code null}
     * @return returns the input without the preprocessor blocks
     */
    public String process(final String in) {
        Validate.notNull(in, "Input must not be null!");
        data.clear();
        final List<String> preprocesorBuffer = Lists.newArrayList();
        final StringBuilder contentBuffer = new StringBuilder();
        boolean inside = false;

        for (final String line : in.split(Constants.DEFAULT_NEW_LINE.toString())) {
            if (line.trim().startsWith(START_TOKEN)) {
                inside = true;
                continue;
            }

            if (line.trim().startsWith(END_TOKEN)) {
                inside = false;
                continue;
            }

            if (inside) {
                if (line.trim().startsWith("//")) {
                    continue; // Skip comments.
                }

                preprocesorBuffer.add(line);
            } else {
                contentBuffer.append(line).append(Constants.DEFAULT_NEW_LINE.toString());
            }
        }

        recognizedData(preprocesorBuffer);
        return contentBuffer.toString().trim();
    }

    /**
     * Returns map with key value pairs recognized after calling {@link #process(java.lang.String)}.
     *
     * @return never {@code null}, defense copy
     */
    public Map<String, String> getData() {
        return Maps.newHashMap(data);
    }

    /**
     * Get the {@link #getData() data} as meta data object.
     *
     * @return never {@code null}, always new instance
     */
    public MetaData getMetaData() {
        return new MetaData(getData());
    }

    /**
     * Recognizes key value pairs.
     *
     * This method adds the recognized data to {@link #data}.
     *
     *
     * @param preprocesorBlocks must not be {@code null}
     * @see #recognizedData(java.util.List, java.util.Map)
     */
    private void recognizedData(final List<String> preprocesorBlocks) {
        recognizedData(preprocesorBlocks, data);
    }

    /**
     * Recognizes key value pairs.
     *
     * Recognized key value pairs will be added to passed map.
     *
     * Lines with errors (no key/value or empty key) line is skipped and warning logged. Empty values are logged but not
     * skipped.
     *
     * @param preprocesorBlocks must not be {@code null}
     * @param map must not be {@code null}
     */
    static void recognizedData(final List<String> preprocesorBlocks, final Map<String, String> map) {
        Validate.notNull(preprocesorBlocks, "Preprocesor block must not be null!");
        Validate.notNull(map, "Map block must not be null!");

        for (final String line : preprocesorBlocks) {
            if (!line.contains(SPLIT_TOKEN)) {
                LOG.warn(String.format("Malformed line '%s'! Missing split token '%s'. Use format 'key%svalue'.",
                        line, SPLIT_TOKEN, SPLIT_TOKEN));
                continue;
            }

            final String[] tokens = line.split(SPLIT_TOKEN);

            if (tokens.length == 0) {
                LOG.warn(String.format("No key given: '%s'! Skipping line.", line));
                continue;
            }

            final String name = tokens[0].trim();

            if (name.isEmpty()) {
                LOG.warn(String.format("Empty key given: '%s'! Skipping line.", line));
                continue;
            }

            final String value;

            if (tokens.length == 1) {
                LOG.warn(String.format("No value given: '%s'! Set vlaue empty.", line));
                value = "";
            } else {
                value = tokens[1].trim();

                if (value.isEmpty()) {
                    LOG.warn(String.format("Empty value given: '%s'! Set vlaue empty.", line));
                }
            }

            map.put(name, value);
        }
    }
}
