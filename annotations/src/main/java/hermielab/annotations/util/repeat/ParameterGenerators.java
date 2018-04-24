package hermielab.annotations.util.repeat;

import hermielab.annotations.util.ParameterGenerator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface ParameterGenerators {
    ParameterGenerator[] value();
}
