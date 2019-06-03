package me.bristermitten.reflector.property;

import me.bristermitten.reflector.property.valued.ValuedClassStructure;

import java.util.Set;

/**
 * Global interface for {@link ValuedClassStructure} and {@link Property}
 * Used in JUI, and other systems that need reflection info without caring if it is a class or a property
 */
public interface Element {
    Class getType();

    boolean isComplexType();



    String getName();

    Object getValue();

    Set<Property> getProperties();
}