package me.bristermitten.reflector.property;

import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.property.info.Informational;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;

import java.util.Set;

/**
 * Global interface for {@link ValuedClassStructure} and {@link Property}
 * Used in JUI, and other systems that need reflection info without caring if it is a class or a property
 */
public interface Element extends Informational {
    /**
     * @return the type of the element
     */
    Class getType();

    /**
     * @return if the element is a complex type
     * @see ReflectionHelper#isSimple(Class)
     */
    boolean isComplexType();

    /**
     * @return the name of the element
     */
    String getName();

    /**
     * @return the value of the element, possibly null
     */
    Object getValue();

    /**
     * @return properties of the element
     */
    Set<Property> getProperties();

    /**
     * @param type the type to check
     * @return if the element is a subclass of a given type
     */
    boolean isSubTypeOf(Class type);
}