package annotations.abstraction.repeat;

import annotations.abstraction.FieldMethodSymbol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface FieldMethodSymbols {
    FieldMethodSymbol[] value();
}
