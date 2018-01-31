package annotations.abstraction;

import annotations.abstraction.repeat.FieldMethodSymbols;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Repeatable(FieldMethodSymbols.class)
public @interface FieldMethodSymbol {
    String inputFieldID();
    String[] fieldMethods(); //The id of this output Function, "the input letter of the input alphabet of the learner"
    String[] outputFunctionIDs();
    String[] params();
}
