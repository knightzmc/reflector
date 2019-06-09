package me.bristermitten.reflector.property.setter;

/**
 * Wraps a setter method or field for setting of a value
 */
public interface Setter {
    Setter EMPTY = newValue -> newValue;

    /**
     * Set a new value
     *
     * @param newValue the new value to set
     * @return the previous value
     */
    Object set(Object newValue);
}
