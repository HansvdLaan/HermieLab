package annotations.concrete;

import annotations.repeat.OutputFunctions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Repeatable(OutputFunctions.class)
public @interface OutputFunction {
    String id(); //The id of this Output Function
    String[] params();
}
