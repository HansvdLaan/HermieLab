package hermielab.annotations.util;

import hermielab.annotations.util.repeat.ParameterGenerators;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.util.ParameterGenerator} is
 * used to create a new parameter which can be referenced in a symbol.
 */
@Target(ElementType.METHOD)
@Repeatable(ParameterGenerators.class)
public @interface ParameterGenerator {
    /**
     * The ID of the parameter. This is the ID you can reference to in
     * parameter declarations in symbols.
     * @return the costum ID of this parameter
     */
    String ID();
}
