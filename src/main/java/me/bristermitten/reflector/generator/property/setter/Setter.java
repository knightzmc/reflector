package me.bristermitten.reflector.generator.property.setter;

public interface Setter {
    Setter EMPTY = newValue -> newValue;

    Object set(Object newValue);
}
