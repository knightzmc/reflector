package me.bristermitten.reflector.property.info.search;

import me.bristermitten.reflector.property.Property;

import java.util.function.Predicate;

public interface SearchCondition extends Predicate<Property> {

    @Override
    default boolean test(Property property) {
        return matches(property);
    }

    boolean matches(Property p);
}
