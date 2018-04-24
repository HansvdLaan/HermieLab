package hermielab.annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.setup.Shutdown} is
 * used to indicate which method should be called to shutdown the SUL.
 */
@Target(ElementType.METHOD)
public @interface Shutdown {
    int order() default -1;
}
