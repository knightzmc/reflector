package me.bristermitten.reflector;

import com.google.common.collect.ImmutableSet;
import lombok.SneakyThrows;
import me.bristermitten.reflector.helper.ReflectionHelper;
import me.bristermitten.reflector.util.TestEnum;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class ReflectionHelperTests {
    private ReflectionHelper helper;
    private Set<Class> primitives = ImmutableSet.of(
            boolean.class, char.class, byte.class, short.class, int.class,
            long.class, float.class, double.class, void.class, Boolean.class, Character.class,
            Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
            Void.class
    );

    @Before
    public void init() {
        Reflector reflector = new Reflector();
        helper = reflector.helper();
    }


    @Test
    public void testReflection_isPrimitive_returnsTrue() {
        for (Class clazz : primitives) {
            assertTrue(helper.isPrimitive(clazz));
        }
    }

    @Test
    public void testReflection_isSimple_returnsTrue() {
        assertTrue(helper.isSimple(String.class));
        assertTrue(helper.isSimple(TestEnum.class));
        for (Class clazz : primitives) {
            assertTrue(helper.isSimple(clazz));
        }
    }

    @Test
    @SneakyThrows
    public void testReflection_invokeMethod_invokes() {
        TestMethodsClass tests = Mockito.spy(TestMethodsClass.class);
        Method doNothingMethod = TestMethodsClass.class.getDeclaredMethod("doNothing");
        helper.invokeMethod(doNothingMethod, tests);
        verify(tests).doNothing();
    }

    @Test
    @SneakyThrows
    public void testReflection_getMethod_returnsCorrectMethod() {
        Method doNothingMethod = TestMethodsClass.class.getDeclaredMethod("doNothing");
        Method foundMethod = helper.getMethod(TestMethodsClass.class, "doNothing");
        assertEquals(doNothingMethod, foundMethod);
    }

    @Test
    public void testReflection_represents_returnsTrueWhenRepresents() {
        final Class primClass = int.class;
        final Class wrappedClass = Integer.class;
        assertTrue(helper.represents(primClass, wrappedClass));
        assertFalse(helper.represents(primClass, String.class));
    }


    @Test
    @SneakyThrows
    public void testReflection_getAnnotations_findCorrectly() {
        TestAnnotationClass testAnnotationClass = new TestAnnotationClass();
        Field field = testAnnotationClass.getClass().getDeclaredField("test");
        Annotation[] annotations = helper.getAnnotations(field);
        assertEquals(1, annotations.length);
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
    }

    public static class TestMethodsClass {
        void doNothing() {

        }

        String returnString(String s) {
            return s;
        }
    }

    public static class TestAnnotationClass {
        @TestAnnotation
        private int test;
    }
}
