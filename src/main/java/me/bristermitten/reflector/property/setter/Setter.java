package me.bristermitten.reflector.property.setter;

public interface Setter {
    Setter EMPTY = newValue -> newValue;

    Object set(Object newValue);
}
