package annotations.abstraction;

import annotations.abstraction.repeat.FunctionSymbols;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Repeatable(FunctionSymbols.class)
public @interface FunctionSymbol {
    String inputSymbolID();
    String inputFunctionID();
    String outputFunctionID(); //The id of this output Function, "the input letter of the input alphabet of the learner"
    // and the id which the mapper uses to translate the id to a concrete method invocation.
    //String[] predicateFunctionIDs(); //The ids of the predicates which must be true before this function can be excecuted
    String[] params();
}
