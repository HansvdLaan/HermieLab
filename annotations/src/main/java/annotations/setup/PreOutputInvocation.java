package annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface PreOutputInvocation {
    int order(); //The order of the pre output function invocations
    String[] params() default {""};
}
