package me.bristermitten.reflector.property;

import com.google.common.primitives.Primitives;
import com.google.inject.Inject;
import lombok.ToString;
import me.bristermitten.reflector.Reflector;
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.property.info.Info;
import me.bristermitten.reflector.property.info.TypeInfo;
import me.bristermitten.reflector.property.setter.Setter;
import me.bristermitten.reflector.property.setter.SetterFactory;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Simple property implementation
 */
@ToString(exclude = {"reflector", "reflectionHelper", "factory"})
public abstract class AbstractProperty implements Property {
    protected final Reflector reflector;
    protected final ReflectionHelper reflectionHelper;
    protected final SetterFactory factory;

    protected final Class type;
    protected final String name;
    protected final Field field;
    protected final @Nullable Method setterMethod;
    protected final @Nullable Method getterMethod;
    protected final Info info;
    private Setter setter;
    private Object source;
    private Object value;

    @Inject
    public AbstractProperty(ReflectionHelper reflectionHelper,
                            SetterFactory factory,
                            Reflector reflector,
                            String name,
                            Field field,
                            @Nullable Method getterMethod,
                            @Nullable Method setterMethod,
                            Info info) {
        this.reflectionHelper = reflectionHelper;
        this.factory = factory;
        this.type = Primitives.wrap(field.getType());
        this.name = name;
        this.field = field;
        this.setterMethod = setterMethod;
        this.getterMethod = getterMethod;
        this.reflector = reflector;
        this.info = info;
    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Setter createSetter() {
        if (setter != null) return setter;
        return setter = createSetter(source);
    }

    @Override
    public abstract Object getValue(Object source);

    @Override
    public Object getValue() {
        if (value != null) return value;
        if (source == null) return null;
        else return value = getValue(source);
    }

    @Override
    public Set<Property> getProperties() {
        return reflector.getValuedStructure(getValue()).getProperties();
    }

    public Object getSource() {
        return source;
    }

    @Override
    public void setSource(Object source) {
        this.source = source;
        setter = null; //remove setter cached for the previous source
        value = null; //and remove cached value
    }

    @Override
    public boolean isComplexType() {
        return !reflectionHelper.isSimple(type);
    }

    @Override
    public Info getInfo() {
        return info;
    }

    @Override
    public boolean isSubTypeOf(Class type) {
        return type.isAssignableFrom(getType());
    }
}
