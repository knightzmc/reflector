package me.bristermitten.reflector.config;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Indicates that the implementation of a marked interface can be customised in
 * {@link Options} for advanced or platform-specific functionality
 */
@Target(TYPE)
public @interface ConfigurableInterface {
}
