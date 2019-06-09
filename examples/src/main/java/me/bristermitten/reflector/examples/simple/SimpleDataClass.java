package me.bristermitten.reflector.examples.simple;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleDataClass {
    private int age;
    private String name;

    public String getName() {
        System.out.println("Name got!");
        return name;
    }
}
