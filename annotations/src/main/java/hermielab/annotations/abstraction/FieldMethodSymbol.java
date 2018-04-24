package hermielab.annotations.abstraction;

import hermielab.annotations.abstraction.repeat.FieldMethodSymbols;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.abstraction.FieldMethodSymbol} is
 * used to create input symbols for methods which could be executed on this field.
 * For example, if you have got a field 'Button b' in your class, you can annotate it with this annotation
 * and create an input symbol for methods executable on this field such as setVisible(boolean).
 */
@Target(ElementType.FIELD)
@Repeatable(FieldMethodSymbols.class)
public @interface FieldMethodSymbol {
    /**
     * The ID for this field. This ID is used in the name generation for the input symbols.
     * @return the costum ID of this field
     */
    String fieldID();
    /**
     * The methods for which input symbols should be created. They should be written in the form of function(type,type).
     * For example: setVisible(boolean)
     * @return the methods to be used as input symbols
     */
    String[] fieldMethods();
    /**
     * The output functions to get the output of the newly defined input symbols.
     * Use "this" to use the output of the input function as output.
     * @return output methods;
     */
    String[] outputIDs();
    /**
     * The parameters to be passed to the given methods.
     * @return parameters of the given methods
     */
    String[] parameters() default {};
    /**
     * Predicates which should hold before any of the input symbols can be passed to the SUT.
     * @return predicates of the input symbols
     */
    String[] predicates() default {};
    String[] nfapredicates() default {};
}
