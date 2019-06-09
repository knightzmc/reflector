package me.bristermitten.reflector;

import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.config.OptionsBuilder;
import me.bristermitten.reflector.constructor.InstanceConstructor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static junit.framework.TestCase.assertTrue;

public class ConstructorTests {

    private Reflector reflector;

    @Before
    public void before() {
        reflector = new Reflector(new OptionsBuilder().lenientConstructorSearch().build());
    }

    @After
    public void reset() {
        ConstructorTest.called = false;
    }

    @Test
    public void testLeniency() {
        InstanceConstructor<ConstructorTest> construct =
                reflector.construct(ConstructorTest.class, String.class);
        construct.create();
        assertTrue(ConstructorTest.called);
    }

    @Test
    public void testLeniency2() {
        InstanceConstructor<ConstructorTest> construct =
                reflector.construct(ConstructorTest.class, String.class, int.class);
        construct.create();
        assertTrue(ConstructorTest.called);
    }

    public static class ConstructorTest {
        private static boolean called;

        public ConstructorTest() {
            called = true;
        }

        public ConstructorTest(String s, int x) {
        }
    }
}
