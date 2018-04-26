package hermielab.annotations.concrete;

import hermielab.annotations.concrete.repeat.OutputFieldMethods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.concrete.OutputFieldMethod} is
 * used to create a new output function which can be referenced in a symbol.
 */
@Target(ElementType.FIELD)
@Repeatable(OutputFieldMethods.class)
public @interface OutputFieldMethod {
    /**
     * The ID for this output field method. This is the ID you can reference to in
     * output declarations in symbols.
     * @return the costum ID of this output function
     */
    String ID();
    /**
     * The method for which the output should be captured.
     * They should be written in the form of function(type,type).
     * For example: setVisible(boolean)
     * @return the method to capture the output from
     */
    String fieldMethod();
}
