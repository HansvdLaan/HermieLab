package hermielab.annotations.concrete.repeat;

import hermielab.annotations.concrete.InputFunction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface InputFunctions {
    InputFunction[] value();
}
