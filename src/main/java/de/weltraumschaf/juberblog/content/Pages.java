package de.weltraumschaf.juberblog.content;

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.validate.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Convenience type to collect pages.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class Pages {

    private final List<Page> data = new ArrayList<>();

    public void add(final Pages pages) {
        data.addAll(pages.data);
    }

    public void add(final Page page) {
        data.add(page);
    }

    public void sort(Comparator<? super Page> comparator) {
        Collections.sort(data, comparator);
    }

    public List<Page> data() {
        return Collections.unmodifiableList(data);
    }

    /**
     * Convert collection of pages into plain java collections for assigning them to the templates.
     *
     * @param converter must not be {@code null}
     * @return never {@code null}, unmodifiable
     */
    public Collection<Map<String, Object>> convert(final PageConverter converter) {
        Validate.notNull(converter, "converter");
        final Collection<Map<String, Object>> items = Lists.newArrayList();

        for (final Page page : data) {
            items.add(converter.convert(page));
        }

        return Collections.unmodifiableCollection(items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Pages)) {
            return false;
        }

        final Pages other = (Pages) obj;
        return Objects.equals(data, other.data);
    }

    @Override
    public String toString() {
        return "Pages{" + "data=" + data + '}';
    }

}
