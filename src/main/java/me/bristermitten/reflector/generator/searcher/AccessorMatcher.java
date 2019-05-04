package me.bristermitten.reflector.generator.searcher;

import me.bristermitten.jui.config.ConfigurableInterface;
import me.bristermitten.jui.generator.property.Property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Configurable interface used in {@link ClassSearcher} to determine if a given method
 * is a getter or setter for a given field, which is then used for creating {@link Property} objects
 * In {@link DefaultAccessorMatcher}, the spec for a getter is: {var} get{varName}()
 * and the spec for a setter is void set{varName}({var} {varName});
 * However if your platform uses fluent accessors, or something else, you may wish to
 * change this implementation so your accessors are found
 */
@ConfigurableInterface
public interface AccessorMatcher {
    boolean isSetter(Method m, Field setterFor);

    boolean isGetter(Method m, Field getterFor);


    /*
            DEFAULT IMPLEMENTATION
     */

    /**
     * Default implementation of {@link AccessorMatcher}
     *
     * @see AccessorMatcher
     */
    class DefaultAccessorMatcher implements AccessorMatcher {
        @Override
        public boolean isSetter(Method m, Field setterFor) {
            if (m.getDeclaringClass() != setterFor.getDeclaringClass()) return false;
            if (m.getParameterCount() == 0) return false;
            if (m.getParameterTypes()[0] != setterFor.getType()) return false;
            if (m.getReturnType() == Void.class) return false;
            String setterVarName = m.getName().replace("set", "");
            return setterFor.getName().equalsIgnoreCase(setterVarName);
        }

        @Override
        public boolean isGetter(Method m, Field getterFor) {
            if (m.getDeclaringClass() != getterFor.getDeclaringClass()) return false;
            if (m.getParameterCount() > 0) return false;
            if (m.getReturnType() != getterFor.getType()) return false;
            String getterVarName = m.getName().replace("get", "");
            //some booleans use "is" instead of "get" eg "isAdmin
            getterVarName = getterVarName.replace("is", "");
            return getterFor.getName().equalsIgnoreCase(getterVarName);
        }
    }
}
