package annotations.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface Parameter {
    String id(); //The id of this Predicate Function
    String[] params() default {""};
}
