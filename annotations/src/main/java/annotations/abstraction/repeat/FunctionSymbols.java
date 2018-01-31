package annotations.abstraction.repeat;

import annotations.abstraction.FunctionSymbol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface FunctionSymbols {
    FunctionSymbol[] value();
}
