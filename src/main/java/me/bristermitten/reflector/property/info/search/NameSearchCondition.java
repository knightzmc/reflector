package me.bristermitten.reflector.property.info.search;

import me.bristermitten.reflector.property.Property;

/**
 * Search condition that matches a property name
 */
public class NameSearchCondition implements SearchCondition {
    private final String name;

    public NameSearchCondition(String name) {
        this.name = name;
    }

    @Override
    public boolean matches(Property p) {
        return name.equals(p.getName());
    }
}
