package me.bristermitten.reflector.generator.property;

import me.bristermitten.reflector.annotation.ReflectorExpose;
import me.bristermitten.reflector.generator.property.setter.Setter;
import me.bristermitten.reflector.generator.property.structure.ClassStructure;
import me.bristermitten.reflector.generator.property.valued.ValuedClassStructure;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A property goes beyond the traditional definition of a field with a getter and setter,
 * simply because I couldn't think of a better name.
 * <p>
 * A property represents a piece of data in an object, which does not necessarily have any
 * getters or setters.
 * <p>
 * A property can be a single field (although single private fields must be marked with
 * {@link ReflectorExpose} to be scanned),
 * <p>
 * A public field without any accessor methods,
 * <p>
 * A private field with a getter only,
 * <p>
 * Or a private field with a setter and getter.
 * <p>
 * A private field without a getter is not supported, as it indicates that the value is not
 * for viewing and should stay encapsulated (this is subject to change).
 * <p>
 * A property in its most abstract form is simply a wrapper of a Field and/or Method object
 * It requires an Object passed to {@link Property#getValue(Object)} in order to retrieve values,
 * which it does so from that Object.
 * However, a property can also be given a predetermined object to retrieve values from, meaning
 * {@link Property#getValue()} will instead be used. Unless manual setting has been done, this
 * default object source will always be null if the property object is retrieved from
 * {@link ClassStructure#getProperties()}, but will have a value if it is retrieved from
 * {@link ValuedClassStructure#getProperties()}
 */
public interface Property {
    Class getType();

    Object getValue(Object source);

    Object getValue();

    String getName();

    Setter createSetter(@Nullable Object settingOn);

    Setter createSetter();

    Object getSource();

    void setSource(Object source);

    Set<Property> getProperties();

    boolean isComplexType();
}
