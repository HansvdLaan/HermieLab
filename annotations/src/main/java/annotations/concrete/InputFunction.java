package annotations.concrete;

import annotations.concrete.repeat.InputFunctions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Repeatable(InputFunctions.class)
public @interface InputFunction {
    String id();
    String[] params() default {""};
}
