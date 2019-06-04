package me.bristermitten.reflector.config;

import me.bristermitten.reflector.searcher.AccessorMatcher;
import me.bristermitten.reflector.searcher.AccessorMatcher.DefaultAccessorMatcher;
import me.bristermitten.reflector.searcher.NameDecider;
import me.bristermitten.reflector.searcher.NameDecider.DefaultNameDecider;

import static me.bristermitten.reflector.config.FieldAccessLevel.NONE;

public class OptionsBuilder {
    private FieldAccessLevel accessLevel = NONE;
    private boolean scanSuperClasses = false;
    private Class<? extends AccessorMatcher> accessorMatcher = DefaultAccessorMatcher.class;
    private Class<? extends NameDecider> nameDecider = DefaultNameDecider.class;

    public Options build() {
        return new Options(
                accessorMatcher,
                nameDecider,
                accessLevel,
                scanSuperClasses
        );
    }

    public OptionsBuilder fieldAccessLevel(FieldAccessLevel accessLevel) {
        this.accessLevel = accessLevel;
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
