package annotations.abstraction.repeat;

import annotations.abstraction.WidgetSymbol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface WidgetSymbols {
    WidgetSymbol[] value();
}
