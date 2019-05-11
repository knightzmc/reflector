<<<<<<< HEAD
package me.bristermitten.reflector.inject;

import com.google.inject.Module;
import com.google.inject.*;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.generator.property.*;
import me.bristermitten.reflector.property.*;
import me.bristermitten.reflector.property.setter.FieldSetter;
import me.bristermitten.reflector.property.setter.Setter;
import me.bristermitten.reflector.property.setter.SetterFactory;
import me.bristermitten.reflector.property.setter.SetterSetter;
import me.bristermitten.reflector.searcher.*;

public class ReflectorBindingModule extends AbstractModule {
    private final Options options;

    private Module customInjectModule;

    public ReflectorBindingModule() {
        this(Options.DEFAULT);
    }

    public ReflectorBindingModule(Options options) {
        this(options, null);
    }

    public ReflectorBindingModule(Options options, Module customInjectModule) {
        this.options = options;
        this.customInjectModule = customInjectModule;
    }

    @Override
    public void configure() {
        if (customInjectModule != null)
            install(customInjectModule);

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

    public Injector createInjector() {
        return Guice.createInjector(Stage.PRODUCTION, this);
    }
}
=======
package me.bristermitten.reflector.inject;

import com.google.inject.Module;
import com.google.inject.*;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import me.bristermitten.reflector.Reflector;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.property.*;
import me.bristermitten.reflector.property.setter.FieldSetter;
import me.bristermitten.reflector.property.setter.Setter;
import me.bristermitten.reflector.property.setter.SetterFactory;
import me.bristermitten.reflector.property.setter.SetterSetter;
import me.bristermitten.reflector.searcher.*;

public class ReflectorBindingModule extends AbstractModule {
    private final Options options;

    private Module customInjectModule;


    public ReflectorBindingModule(Options options) {
        this(options, null);
    }

    public ReflectorBindingModule(Options options, Module customInjectModule) {
        this.options = options;
        this.customInjectModule = customInjectModule;
    }

    @Override
    public void configure() {
        if (customInjectModule != null)
            install(customInjectModule);

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

    public Injector createInjector() {
        return Guice.createInjector(this);
    }
}
