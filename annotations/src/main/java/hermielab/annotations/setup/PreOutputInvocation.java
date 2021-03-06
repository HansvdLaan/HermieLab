package hermielab.annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.setup.PreOutputInvocation} type is used to
 * indicate which methods should be called before any output method of any input symbol
 * is invocated on the SUL as to ensure query equivalence.
 */
@Target(ElementType.METHOD)
public @interface PreOutputInvocation {
    /**
     * Sets the position when this annotated method should be executed in the
     * list of all methods annotated with this annotation.
     * If this value is left empty, it will be executed last.
     * @return position when this method should be executed.
     */
    int order() default -1;
}
