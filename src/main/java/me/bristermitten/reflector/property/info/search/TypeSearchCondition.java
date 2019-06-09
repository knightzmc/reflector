package me.bristermitten.reflector.property.info.search;

import com.google.common.primitives.Primitives;
import me.bristermitten.reflector.property.Property;

import java.util.Arrays;

/**
 * Search condition that matches property types
 */
public class TypeSearchCondition implements SearchCondition {
    private final Class[] types;

    public TypeSearchCondition(Class... types) {
        this.types = types;
    }

    @Override
    public boolean matches(Property p) {
        final Class type = p.getType();
        return Arrays.stream(types).map(Primitives::wrap).allMatch(type::equals);
    }
}
