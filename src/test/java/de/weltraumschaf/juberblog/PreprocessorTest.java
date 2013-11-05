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

import de.weltraumschaf.juberblog.model.MetaData;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Preprocessor}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PreprocessorTest {

    private static final String NL = Constants.DEFAULT_NEW_LINE.toString();
    private final Preprocessor sut = new Preprocessor();

    @Test
    public void process_removeBlocksAndGetData() {
        assertThat(sut.getData().size(), is(0));
        assertThat(sut.process("<?juberblog" + NL
                + "  key1: value1" + NL
                + "  key2: " + NL
                + "  // comment" + NL
                + "  key3: value3" + NL
                + "?>" + NL
                + "snafu" + NL
                + "<?juberblog" + NL
                + "  key4: value4" + NL
                + "?>" + NL
                + "foobar"), is(equalTo("snafu\nfoobar")));
        assertThat(sut.getData().size(), is(4));
        assertThat(sut.getData(), hasEntry("key1", "value1"));
        assertThat(sut.getData(), hasEntry("key2", ""));
        assertThat(sut.getData(), hasEntry("key3", "value3"));
        assertThat(sut.getData(), hasEntry("key4", "value4"));
    }

    @Test
    public void getMetaData() {
        sut.process("<?juberblog" + NL
                + "    Navi: Projects" + NL
                + "    Description: My personal projects I'm working on" + NL
                + "    Keywords: Projects, Jenkins, Darcs" + NL
                + "?>");
        final MetaData data = sut.getMetaData();
        assertThat(data.getNavi(), is(equalTo("Projects")));
        assertThat(data.getDescription(), is(equalTo("My personal projects I'm working on")));
        assertThat(data.getKeywords(), is(equalTo("Projects, Jenkins, Darcs")));
    }

    @Test
    public void process_ignoreMissingKey() {
        sut.process("<?juberblog" + NL
                + "    Navi: Projects" + NL
                + ": My personal projects I'm working on" + NL
                + "    Keywords: Projects, Jenkins, Darcs" + NL
                + "?>");
        assertThat(sut.getData().size(), is(2));
        assertThat(sut.getData(), hasEntry("Navi", "Projects"));
        assertThat(sut.getData(), hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void process_ignoreEmptyKey() {
        sut.process("<?juberblog" + NL
                + "    Navi: Projects" + NL
                + "        : My personal projects I'm working on" + NL
                + "    Keywords: Projects, Jenkins, Darcs" + NL
                + "?>");
        assertThat(sut.getData().size(), is(2));
        assertThat(sut.getData(), hasEntry("Navi", "Projects"));
        assertThat(sut.getData(), hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void process_ignoreMissingValue() {
        sut.process("<?juberblog" + NL
                + "    Navi: Projects" + NL
                + "    Description" + NL
                + "    Keywords: Projects, Jenkins, Darcs" + NL
                + "?>");
        assertThat(sut.getData().size(), is(2));
        assertThat(sut.getData(), hasEntry("Navi", "Projects"));
        assertThat(sut.getData(), hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void process_emptyLine() {
        sut.process("<?juberblog" + NL
                + "    Navi: Projects" + NL
                + "    " + NL
                + "    Keywords: Projects, Jenkins, Darcs" + NL
                + "?>");
        assertThat(sut.getData().size(), is(2));
        assertThat(sut.getData(), hasEntry("Navi", "Projects"));
        assertThat(sut.getData(), hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void process_emptyLines() {
        sut.process("<?juberblog" + NL
                + "    Navi: Projects" + NL
                + "    " + NL
                + NL
                + "    " + NL
                + "    " + NL
                + "    Keywords: Projects, Jenkins, Darcs" + NL
                + "?>");
        assertThat(sut.getData().size(), is(2));
        assertThat(sut.getData(), hasEntry("Navi", "Projects"));
        assertThat(sut.getData(), hasEntry("Keywords", "Projects, Jenkins, Darcs"));
    }

    @Test
    public void recognizedData_emptyBlock() {
        final List<String> preprocesorBlocks = Lists.newArrayList();
        final Map<String, String> map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
        assertThat(map.isEmpty(), is(true));
    }

    @Test
    public void recognizedData_emptyLines() {
        final List<String> preprocesorBlocks = Arrays.asList("", "", "");
        final Map<String, String> map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
    }

    @Test
    public void recognizedData_linesWithwhitespaces() {
        final List<String> preprocesorBlocks = Arrays.asList("    ", "\t", "  \t  ", "\n", "   \n", "  \t  \n  ");
        final Map<String, String> map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
    }

    @Test
    public void recognizedData_missingKey() {
        final List<String> preprocesorBlocks = Lists.newArrayList(":foobar");
        final Map<String, String> map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
        assertThat(map.isEmpty(), is(true));
    }

    @Test
    public void recognizedData_missingSplitToken() {
        final List<String> preprocesorBlocks = Lists.newArrayList("foobar");
        final Map<String, String> map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
        assertThat(map.isEmpty(), is(true));
    }

    @Test
    public void recognizedData_missingMultipleSplitTokens() {
        List<String> preprocesorBlocks = Lists.newArrayList("snafu:foobar:bla:blub");
        Map<String, String> map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
        assertThat(map, hasEntry("snafu", "foobar"));

        preprocesorBlocks = Lists.newArrayList("::snafu");
        map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
        assertThat(map.isEmpty(), is(true));

        preprocesorBlocks = Lists.newArrayList("::");
        map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
        assertThat(map.isEmpty(), is(true));

        preprocesorBlocks = Lists.newArrayList(":::");
        map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
        assertThat(map.isEmpty(), is(true));

        preprocesorBlocks = Lists.newArrayList("foo::bar");
        map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
        assertThat(map, hasEntry("foo", ""));
    }

    @Test
    public void recognizedData_missingValue() {
        final List<String> preprocesorBlocks = Lists.newArrayList("foobar:");
        final Map<String, String> map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
        assertThat(map, hasEntry("foobar", ""));
    }

    @Test
    public void recognizedData_oneKeyValue() {
        final List<String> preprocesorBlocks = Lists.newArrayList("foo  :   bar");
        final Map<String, String> map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
    }

    @Test
    public void recognizedData_threeKeyValue() {
        final List<String> preprocesorBlocks = Lists.newArrayList("foo  :   bar", "  snafu:foobar", "\tbaz: 1234");
        final Map<String, String> map = Maps.newHashMap();
        Preprocessor.recognizedData(preprocesorBlocks, map);
        assertThat(map, hasEntry("foo", "bar"));
        assertThat(map, hasEntry("snafu", "foobar"));
        assertThat(map, hasEntry("baz", "1234"));
    }

}
