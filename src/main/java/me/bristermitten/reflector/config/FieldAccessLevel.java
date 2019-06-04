package me.bristermitten.reflector.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * Defines an access level of fields that should be included
 */
public interface FieldAccessLevel extends Predicate<Field> {

    /**
     * Include all fields, no matter the access modifier
     */
    FieldAccessLevel ALL = field -> true;
    /**
     * Field must be public
     */
    FieldAccessLevel PUBLIC = field -> Modifier.isPublic(field.getModifiers());
    /**
     * Field must be protected or public
     */
    FieldAccessLevel PROTECTED = field -> Modifier.isProtected(field.getModifiers()) || PUBLIC.test(field);
    /**
     * Field must be package private, protected, or public (not private)
     */
    FieldAccessLevel PACKAGE_PRIVATE = field -> !Modifier.isPrivate(field.getModifiers());

    /**
     * No fields. Ever.
     */
    FieldAccessLevel NONE = field -> false;
}
