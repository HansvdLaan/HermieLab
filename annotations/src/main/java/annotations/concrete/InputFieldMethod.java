package annotations.concrete;

import annotations.concrete.repeat.InputFieldMethods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Repeatable(InputFieldMethods.class)
public @interface InputFieldMethod {
    String fieldID();
    String[] fieldMethods();
    String[] params() default {""};
}
