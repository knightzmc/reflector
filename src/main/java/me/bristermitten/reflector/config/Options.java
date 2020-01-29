package me.bristermitten.reflector.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.bristermitten.reflector.property.structure.ClassStructure;
import me.bristermitten.reflector.searcher.AccessorMatcher;
import me.bristermitten.reflector.searcher.NameDecider;

/**
 * Class for configuring and storing options and implementations for Reflector.
 * All implementation options are final as they are bound and instantiated with Guice.
 * <p>
 * Note that any other options are not necessarily used if any custom implementations
 * are configured.
 * That is, custom implementations may not respect certain option values (but should)
 */
@Accessors(fluent = true)
@Getter
@AllArgsConstructor
public class Options {
    public static final Options DEFAULT = new OptionsBuilder().build();

    /**
     * Class defining an implementation of {@link AccessorMatcher}
     * Will never be null, the default value will be used if not explicitly set
     */
    private final Class<? extends AccessorMatcher> accessorMatcherClass;
    /**
     * Class defining an implementation of {@link NameDecider}
     * Will never be null, the default value will be used if not explicitly set
     */
    private final Class<? extends NameDecider> nameDeciderClass;
    /**
     * The access modifier that fields must have to be included in a {@link ClassStructure}
     */
    private FieldAccessLevel fieldAccessLevel;
    /**
     * If we should also add properties of superclasses to a {@link ClassStructure}
     */
    private boolean scanSuperClasses;

    /**
     * If constructor searching should be lenient (dangerous to enable)
     * An example would be the class
     * <pre>
     *     class TestClass {
     *
     *     TestClass(int x){}
     *     TestClass(){}
     *     }
     * </pre>
     * As we can see, two constructors exist. If we called
     * {@code ClassStructure#constructorFor(String.class)} the system would recognise that no
     * constructor exists with 1 String parameter, so would fallback to the no-args constructor
     * Obviously, this is dangerous as unintended functionality can be created
     */
    private boolean lenientConstructorSearch;

    /**
     * If the annotations of super interfaces should be included in the annotations of a {@link ClassStructure}.
     *
     * Duplicates will be overridden.
     */
    private boolean scanSuperInterfaceAnnotations;


}
