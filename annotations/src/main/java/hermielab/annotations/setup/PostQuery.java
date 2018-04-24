package hermielab.annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.setup.PostQuery} is
 * used to indicate which method should be called after each Query on the SUL as to ensure
 * query equivalence.
 */
@Target(ElementType.METHOD)
public @interface PostQuery {
    int order() default -1;
}
