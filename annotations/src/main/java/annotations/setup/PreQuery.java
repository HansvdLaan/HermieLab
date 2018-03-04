package annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface PreQuery {
    int order(); //The order of the pre query function invocations.
    String[] params() default {""};
}
