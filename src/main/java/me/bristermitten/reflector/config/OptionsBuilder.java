package me.bristermitten.reflector.config;

import me.bristermitten.reflector.searcher.AccessorMatcher;
import me.bristermitten.reflector.searcher.AccessorMatcher.DefaultAccessorMatcher;
import me.bristermitten.reflector.searcher.NameDecider;
import me.bristermitten.reflector.searcher.NameDecider.DefaultNameDecider;

import static me.bristermitten.reflector.config.FieldAccessLevel.NONE;

/**
 * Simple builder for {@link Options}
 * Allows no null values to be set
 */
public class OptionsBuilder {
    private FieldAccessLevel accessLevel = NONE;
    private boolean scanSuperClasses = false, lenientConstructorSearch = false, scanSuperInterfaceAnnotations;
    private Class<? extends AccessorMatcher> accessorMatcher = DefaultAccessorMatcher.class;
    private Class<? extends NameDecider> nameDecider = DefaultNameDecider.class;

    public Options build() {
        return new Options(accessorMatcher, nameDecider, accessLevel, scanSuperClasses, lenientConstructorSearch,
                scanSuperInterfaceAnnotations);
    }

    /**
     * @see Options#fieldAccessLevel()
     */
    public OptionsBuilder fieldAccessLevel(FieldAccessLevel accessLevel) {
        if (accessLevel != null)
            this.accessLevel = accessLevel;
        return this;
    }

    /**
     * @see Options#scanSuperClasses()
     */
    public OptionsBuilder scanSuperClasses() {
        this.scanSuperClasses = true;
        return this;
    }

    /**
     * @see Options#accessorMatcherClass()
     */
    public OptionsBuilder accessorMatcher(Class<? extends AccessorMatcher> accessorMatcher) {
        if (accessorMatcher != null)
            this.accessorMatcher = accessorMatcher;
        return this;
    }

    /**
     * @see Options#nameDeciderClass()
     */
    public OptionsBuilder nameDecider(Class<? extends NameDecider> nameDecider) {
        if (nameDecider != null)
            this.nameDecider = nameDecider;
        return this;
    }

    /**
     * @see Options#lenientConstructorSearch()
     */
    public OptionsBuilder lenientConstructorSearch() {
        this.lenientConstructorSearch = true;
        return this;
    }

    /**
     * @see Options#scanSuperInterfaceAnnotations()
     */
    public OptionsBuilder scanSuperInterfaceAnnotations() {
        this.scanSuperInterfaceAnnotations = true;
        return this;
    }
}
