package me.bristermitten.reflector.examples.simple;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleDataClass {
    private int age;
    private String name;
    private SimpleDataClass parent;

    public String getName() {
        return name;
    }
}
