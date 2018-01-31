package annotations.repeat;

import annotations.concrete.OutputFunction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface OutputFunctions {
    OutputFunction[] value();
}
