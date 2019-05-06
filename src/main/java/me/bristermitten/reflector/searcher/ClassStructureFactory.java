package me.bristermitten.reflector.searcher;

import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.structure.ClassStructure;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;

import java.util.Set;

public interface ClassStructureFactory {

    ClassStructure createStructure(Class type, Set<Property> properties);

    ValuedClassStructure createValuedStructure(Class type, Set<Property> properties,
                                               Object valuesFrom);
}