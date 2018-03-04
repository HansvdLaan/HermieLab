package annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface PreInputInvocation {
    int order(); //The order of the pre input function invocations
    String[] params() default {""};
}
