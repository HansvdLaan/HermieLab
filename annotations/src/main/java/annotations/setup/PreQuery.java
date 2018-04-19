package annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface PreQuery {
    int order() default -1;
    String[] params() default {""};
}
