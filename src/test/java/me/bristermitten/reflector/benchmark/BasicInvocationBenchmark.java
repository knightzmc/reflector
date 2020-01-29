package me.bristermitten.reflector.benchmark;

import lombok.val;
import me.bristermitten.reflector.Reflector;
import me.bristermitten.reflector.util.TestUtils;
import org.junit.Test;

/**
 * @author Alexander Wood (BristerMitten)
 */

public class BasicInvocationBenchmark {

    public static final double REPETITIONS = 10000000;

    @Test
    public void benchmarkBasicInvocation() {
        val reflector = new Reflector();

        val originalDummy = TestUtils.randomDummyObject();

        val dummy = originalDummy.copy();
        long startTime = System.currentTimeMillis();

        val age = reflector.getValuedStructure(dummy)
                .searchProperties().byName("Age")
                .search().findFirst().orElseThrow(RuntimeException::new);

        for (int i = 0; i < REPETITIONS; i++) {
            age.createSetter().set(i);
        }
        long endTime = System.currentTimeMillis();

        long time = (endTime - startTime);
        System.out.println("Setting of Fields with Reflector:");
        System.out.println("Performed " + REPETITIONS + " sets in " + time + " ms");
        System.out.println("That's an average of" + (time / REPETITIONS) + "ms/op");

        val directDummy = originalDummy.copy();
        long startTime2 = System.currentTimeMillis();

        for (int i = 0; i < REPETITIONS; i++) {
            directDummy.setAge(i);
        }

        long endTime2 = System.currentTimeMillis();
        long time2 = (endTime2 - startTime2);

        System.out.println("In Comparison, direct object access:");
        System.out.println("Performed " + REPETITIONS + " sets in " + time2 + " ms");
        System.out.println("That's an average of" + (time2 / REPETITIONS) + "ms/op");


        double reflectorVsDirect = 100 * (double) time / (double) time2;

        System.out.println("Therefore, Reflector was " + (int) reflectorVsDirect + "% slower than direct object " +
                "access");
    }
}
