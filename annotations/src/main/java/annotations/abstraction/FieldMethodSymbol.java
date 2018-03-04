package annotations.abstraction;

import annotations.abstraction.repeat.FieldMethodSymbols;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Repeatable(FieldMethodSymbols.class)
public @interface FieldMethodSymbol {
    String inputFieldID();
    String[] fieldMethods();
    String[] outputFunctionIDs();
    String[] params() default {""};
}
