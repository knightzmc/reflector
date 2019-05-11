package me.bristermitten.reflector.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.bristermitten.reflector.searcher.AccessorMatcher;
import me.bristermitten.reflector.searcher.NameDecider;
import me.bristermitten.reflector.searcher.Searcher;

/**
 * Class for configuring and storing options and implementations for JUI.
 * All implementation options are final as they are bound with Guice.
 * <p>
 * Note that any other options are not necessarily used if any custom implementations
 * are configured.
 * That is, only the default implementation of {@link Searcher} is guaranteed to respect
 * the value of {@link Options#scanSuperClasses}
 */
@Accessors(fluent = true)
@Getter
@AllArgsConstructor
public class Options {
    public static final Options DEFAULT = new OptionsBuilder().build();

    private final Class<? extends AccessorMatcher> accessorMatcherClass;
    private final Class<? extends NameDecider> nameDeciderClass;
    private boolean includeFields;
    private boolean scanSuperClasses;
}
