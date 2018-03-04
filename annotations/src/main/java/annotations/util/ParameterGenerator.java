package annotations.util;

import annotations.util.repeat.ParameterGenerators;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Repeatable(ParameterGenerators.class)
public @interface ParameterGenerator {
    String id(); //The id of this Predicate Function
    String[] params() default {""};
}
