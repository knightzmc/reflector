package me.bristermitten.reflector.searcher;

import me.bristermitten.reflector.config.ConfigurableInterface;

import java.lang.reflect.Field;

/**
 * Configurable interface in charge of converting a {@link Field} into a plain text user friendly String
 * <p>
 * An example can be given with a Field firstName;
 * If we were to call {@link Field#getName()}, it would return "firstName", which isn't
 * particularly user friendly. A more appropriate name would be "First Name", which
 * is what a {@link NameDecider} is used for.
 */
@ConfigurableInterface
public interface NameDecider {

    Class<NoChangeNameDecider> NoChange = NoChangeNameDecider.class;

    String makeName(Field field);

    /**
     * Default Implementation of {@link NameDecider}
     */
    class DefaultNameDecider implements NameDecider {

        @Override
        public String makeName(Field field) {
            String name = field.getName();
            String[] parts = name.split("(?=[A-Z])");

            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                char[] chars = part.toCharArray();
                chars[0] = Character.toUpperCase(chars[0]);
                parts[i] = new String(chars);
            }
            name = String.join(" ", parts);
            return name;
        }
    }

    /**
     * Preset implementation that makes no changes, and just returns the field name
     */
    class NoChangeNameDecider implements NameDecider {
        @Override
        public String makeName(Field field) {
            return field.getName();
        }
    }
}
