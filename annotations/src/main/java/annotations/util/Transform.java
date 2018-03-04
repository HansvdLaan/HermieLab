package annotations.util;

import annotations.util.repeat.Transforms;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Repeatable(Transforms.class)
public @interface Transform {
    String id(); //The id of this Predicate Function
    String[] params() default {""};
}
