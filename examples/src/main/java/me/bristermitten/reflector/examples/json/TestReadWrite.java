package me.bristermitten.reflector.examples.json;

import me.bristermitten.reflector.examples.simple.SimpleDataClass;

public class TestReadWrite {

    public static void main(String[] args) {
        SimpleDataClass simpleDataClass = new SimpleDataClass(2, "Child", new SimpleDataClass(3, "Parent", null));
        JsonSerializer jsonSerializer = new JsonSerializer();
        String json = jsonSerializer.toJson(simpleDataClass);
        System.out.println(json);

        jsonSerializer.fromJson(json, SimpleDataClass.class);
    }
}
