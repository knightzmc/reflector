package me.bristermitten.reflector.generator.searcher;

import me.bristermitten.reflector.generator.property.Property;
import me.bristermitten.reflector.generator.property.structure.ClassStructure;
import me.bristermitten.reflector.generator.property.valued.ValuedClassStructure;

import java.util.Set;

public interface ClassStructureFactory {

    ClassStructure createStructure(Class type, Set<Property> properties);

    ValuedClassStructure createValuedStructure(Class type, Set<Property> properties,
                                               Object valuesFrom);
}
