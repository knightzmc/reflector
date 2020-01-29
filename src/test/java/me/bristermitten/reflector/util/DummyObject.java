package me.bristermitten.reflector.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class DummyObject {
    private String name;
    private int age;
    private boolean admin;
    @Nullable
    private DummyObject parent;


    public DummyObject copy() {
        return new DummyObject(name, age, admin, parent);
    }
}
