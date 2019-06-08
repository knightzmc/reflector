package me.bristermitten.reflector.property;

import me.bristermitten.reflector.annotation.ReflectorExpose;
import me.bristermitten.reflector.property.info.Info;
import me.bristermitten.reflector.property.info.Informational;
import me.bristermitten.reflector.property.setter.Setter;
import me.bristermitten.reflector.property.structure.ClassStructure;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;
import org.jetbrains.annotations.Nullable;

/**
 * A property goes beyond the traditional definition of a field with a getterM and setterM,
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
 * A private field with a getterM only,
 * <p>
 * Or a private field with a setterM and getterM.
 * <p>
 * A private field without a getterM is not supported, as it indicates that the value is not
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
public interface Property extends Element, Informational {

    Object getValue(Object source);

    Setter createSetter(@Nullable Object settingOn);

    Setter createSetter();

    Object getSource();

    void setSource(Object source);

    boolean isComplexType();

    Info getInfo();
}
