package hermielab.annotations.concrete.repeat;

import hermielab.annotations.concrete.OutputFieldMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface OutputFieldMethods {
    OutputFieldMethod[] value();
}
