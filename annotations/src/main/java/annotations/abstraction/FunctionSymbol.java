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
    String outputFunctionID();
    String[] params();
}
