package me.bristermitten.reflector.config;

import com.google.inject.Inject;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Indicates that the implementation of a marked interface can be customised in
 * {@link Options} for advanced, custom, or platform-specific functionality
 * <p>
 * Note that an interface marked with {@link ConfigurableInterface} will be created with Guice,
 * so can have an {@link Inject} constructor if necessary
 */
@Target(TYPE)
public @interface ConfigurableInterface {
}
