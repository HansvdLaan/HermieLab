package hermielab.annotations.abstraction.repeat;

import hermielab.annotations.abstraction.FunctionSymbol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface FunctionSymbols {
    FunctionSymbol[] value();
}
