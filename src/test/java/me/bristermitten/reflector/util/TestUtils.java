package me.bristermitten.reflector.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TestUtils {

    public static DummyObject randomDummyObject() {
        return randomDummyObject(false);
    }

    public static DummyObject randomDummyObject(boolean forceParent) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        boolean admin = random.nextBoolean();
        String name = UUID.randomUUID().toString();
        int age = random.nextInt();
        DummyObject parent = null;

        if (random.nextBoolean() || forceParent) { //if we should make a parent
            parent = randomDummyObject();
        }
        return new DummyObject(name, age, admin, parent);
    }


}
