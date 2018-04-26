package hermielab.annotations.concrete;

import hermielab.annotations.concrete.repeat.OutputFunctions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.concrete.OutputFunction} is
 * used to create a new output function which can be referenced in a symbol.
 */
@Target(ElementType.METHOD)
@Repeatable(OutputFunctions.class)
public @interface OutputFunction {
    /**
     * The ID for this output function. This is the ID you can reference to in
     * output declarations in symbols.
     * @return the costum ID of this output function
     */
    String ID();
}
