package me.bristermitten.reflector.examples.json;

import me.bristermitten.reflector.Reflector;
import me.bristermitten.reflector.property.Property;
import me.bristermitten.reflector.property.structure.ClassStructure;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;

public class JsonSerializer {
    private final Reflector reflector = new Reflector();

    public String toJson(Object o) {
        ValuedClassStructure structure = reflector.getValuedStructure(o);
        JsonWriter writer = new JsonWriter();
        writer.openObject();

        for (Property property : structure.getProperties()) {
            writeProperty(property, writer);
        }
        writer.closeObject();
        return writer.complete();
    }

    private void writeProperty(Property property, JsonWriter writer) {
        if (property.isComplexType()) writeComplexProperty(property, writer);
        else writeSimpleProperty(property, writer);
    }

    private void writeSimpleProperty(Property property, JsonWriter writer) {
        writer.name(property.getName());
        switch (property.getPropertyType()) {
            case NUMBER:
                writer.value((Number) property.getValue());
                break;
            case BOOLEAN:
                writer.value((Boolean) property.getValue());
                break;
            case CHARACTER:
                writer.value(((Character) property.getValue()).toString());
                break;
            case STRING:
                writer.value((String) property.getValue());
                break;
            default: //this will never happen
                break;
        }
    }

    private void writeComplexProperty(Property property, JsonWriter writer) {
        writer.name(property.getName());

        if (property.getValue() == null) {
            writer.writeNull();
            return;
        }

        writer.openObject();
        ValuedClassStructure structure = reflector.getValuedStructure(property.getValue()); //get a structure of the field value for recursion
        for (Property subProperty : structure.getProperties()) {
            writeProperty(subProperty, writer);
        }
        writer.closeObject();
    }


    public <T> T fromJson(String json, Class<T> type) {
        ClassStructure structure = reflector.getStructure(type);
        T t = reflector.construct(type).create(); //generic wrapper of calling structure.constructorFor().create()
        JsonReader reader = new JsonReader(json);
        reader.openObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            System.out.println("reader.nextName() = " + name);
            System.out.println("reader = " + reader.next());
        }
//        System.out.println("reader.nextName() = " + reader.nextName());
        return t;
    }
}
