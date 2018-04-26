package hermielab.annotations.abstraction;

import hermielab.annotations.abstraction.repeat.FunctionSymbols;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.abstraction.FunctionSymbol} is
 * used to create a new input symbol for this function.
 */
@Target(ElementType.METHOD)
@Repeatable(FunctionSymbols.class)
public @interface FunctionSymbol {
    /**
     * Sets the ID of this Symbol.
     * @return the ID of this symbol
     */
    String symbolID();
    /**
     * The output function to get the output this symbol.
     * Use the default "this" to use the output of the input function as output of
     * the whole symbol
     * @return output methods;
     */
    String outputID() default "this";
    /**
     * The parameters to be passed to the annotated method.
     * Examples of parameters are: "int:5, string:some_string, char:v, javaFX:mouse_press".
     * @return parameters of the given method
     */
    String[] parameters() default {};
    /**
     * Predicates which should hold before this input symbols can be invoked on the SUT.
     * @return predicates of the input symbol
     */
    String[] predicates() default {};
    String[] nfapredicates() default {};
}
