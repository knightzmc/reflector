package me.bristermitten.reflector.config;

import me.bristermitten.reflector.generator.searcher.AccessorMatcher;
import me.bristermitten.reflector.generator.searcher.AccessorMatcher.DefaultAccessorMatcher;
import me.bristermitten.reflector.generator.searcher.NameDecider;
import me.bristermitten.reflector.generator.searcher.NameDecider.DefaultNameDecider;

public class OptionsBuilder {
    private boolean includeFields = false;
    private boolean scanSuperClasses = false;
    private Class<? extends AccessorMatcher> accessorMatcher = DefaultAccessorMatcher.class;
    private Class<? extends NameDecider> nameDecider = DefaultNameDecider.class;

    public Options build() {
        return new Options(
                accessorMatcher,
                nameDecider,
                includeFields,
                scanSuperClasses
        );
    }

    public OptionsBuilder includeFields() {
        this.includeFields = true;
        return this;
    }

    public OptionsBuilder scanSuperClasses() {
        this.scanSuperClasses = true;
        return this;
    }

    public OptionsBuilder accessorMatcher(Class<? extends AccessorMatcher> accessorMatcher) {
        this.accessorMatcher = accessorMatcher;
        return this;
    }

    public OptionsBuilder nameDecider(Class<? extends NameDecider> nameDecider) {
        this.nameDecider = nameDecider;
        return this;
    }
}
