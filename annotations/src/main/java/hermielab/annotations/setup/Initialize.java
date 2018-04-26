package hermielab.annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.setup.Initialize} is
 * used to indicate which methods should be called to intialize the SUL. This
 * methods will be called after the method annotated with {@code hermielab.annotations.setup.Start}.
 */
@Target(ElementType.METHOD)
public @interface Initialize {
    /**
     * Sets the position when this annotated method should be executed in the
     * list of all methods annotated with this annotation.
     * If this value is left empty, it will be executed last.
     * @return position when this method should be executed.
     */
    int order() default -1;
}
