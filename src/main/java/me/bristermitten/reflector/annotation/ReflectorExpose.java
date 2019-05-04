package me.bristermitten.reflector.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Forces Reflector to create properties of the marked item
 * This annotation violates best practices of making beans and JUI-compatible classes,
 * and so should only be used as a workaround where exposing a getter isn't possible.
 * <p>
 * An example usage is as follows
 * <pre>
 * {@code
 * public class Test {
 * @ReflectorExpose private int count;
 * }}
 * </pre>
 * which returns a property of <pre>count</pre> when scanned
 * <p>
 * However, a more desirable approach would be
 * <pre>
 *     {@code
 *     public class Test {
 *     private int count;
 *
 *     public int getCount() {
 *         return count;
 *     }
 * }}
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ReflectorExpose {
}
