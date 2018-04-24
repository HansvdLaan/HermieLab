package hermielab.annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.setup.Initialize} is
 * used to indicate which method should be called to intialize the SUL. This
 * methods will be called after the method annotated with {@code hermielab.annotations.setup.Start}.
 */
@Target(ElementType.METHOD)
public @interface Initialize {
    int order() default -1;
}
