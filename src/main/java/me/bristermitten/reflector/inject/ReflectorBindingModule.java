package me.bristermitten.reflector.inject;

import com.google.inject.Module;
import com.google.inject.*;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.property.*;
import me.bristermitten.reflector.property.setter.FieldSetter;
import me.bristermitten.reflector.property.setter.Setter;
import me.bristermitten.reflector.property.setter.SetterFactory;
import me.bristermitten.reflector.property.setter.SetterSetter;
import me.bristermitten.reflector.searcher.*;

/**
 * Main Guice module for Reflector
 */
public class ReflectorBindingModule extends AbstractModule {
    private final Options options;

    /**
     * Simple constructor that uses default options
     */
    public ReflectorBindingModule() {
        this(Options.DEFAULT);
    }

    /**
     * Configures custom options
     *
     * @param options Options
     */
    public ReflectorBindingModule(Options options) {
        this.options = options;
    }

    @Override
    public void configure() {
        bind(Options.class).toInstance(options);
        bind(Searcher.class)
                .annotatedWith(Names.named("FieldSearcher"))
                .to(FieldSearcher.class);

        bind(Searcher.class)
                .annotatedWith(Names.named("MethodSearcher"))
                .to(MethodSearcher.class);

        bind(AccessorMatcher.class).to(options.accessorMatcherClass());
        bind(NameDecider.class).to(options.nameDeciderClass());

        install(new FactoryModuleBuilder()
                .implement(Setter.class, Names.named("FieldSetter"),
                        FieldSetter.class)
                .implement(Setter.class, Names.named("SetterSetter"),
                        SetterSetter.class)
                .build(SetterFactory.class));

        install(new FactoryModuleBuilder()
                .implement(Property.class, Names.named("FullAccessorProperty"),
                        FullAccessorProperty.class)
                .implement(Property.class, Names.named("GetterProperty"),
                        GetterProperty.class)
                .implement(Property.class, Names.named("FieldProperty"),
                        FieldProperty.class)
                .build(PropertyFactory.class));

        install(new FactoryModuleBuilder()
                .build(ClassStructureFactory.class));

    }

    /**
     * Create an Injector based on this module
     * @return a new Injector
     */
    public Injector createInjector() {
        return Guice.createInjector(this);
    }
}
