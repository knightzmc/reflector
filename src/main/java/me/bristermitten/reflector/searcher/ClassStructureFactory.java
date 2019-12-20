package me.bristermitten.reflector.searcher;

import me.bristermitten.reflector.annotation.Nullable;
import me.bristermitten.reflector.constructor.InstanceConstructor;
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.info.Info;
import me.bristermitten.reflector.property.structure.ClassStructure;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;

import java.util.Set;

/**
 * Guice factory for assisted injection of ClassStructure types
 */
public interface ClassStructureFactory {

    /**
     * Create a new ClassStructure
     *
     * @param type         the class the structure wraps
     * @param properties   the properties in the class
     * @param info         info about the base class
     * @param constructors any constructors the class has
     * @return a new ClassStructure
     */
    ClassStructure createStructure(Class<?> type, Set<Property> properties,
                                   Info info, Set<InstanceConstructor<?>> constructors,
                                   boolean isFullClass);

    /**
     * Create a new ValuedClassStructure
     *
     * @param type         the class the structure wraps
     * @param properties   the properties in the class
     * @param info         info about the base class
     * @param constructors any constructors the class has
     * @param valuesFrom   an object to obtain values from for properties
     * @return a new ValuedClassStructure
     */
    ValuedClassStructure createValuedStructure(Class<?> type, Set<Property> properties,
                                               Info info, Set<InstanceConstructor<?>> constructors,
                                               @Nullable Object valuesFrom,
                                               boolean isFullClass);
}
