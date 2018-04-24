package hermielab.annotations.concrete.repeat;

import hermielab.annotations.concrete.InputFieldMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface InputFieldMethods {
    InputFieldMethod[] value();
}
