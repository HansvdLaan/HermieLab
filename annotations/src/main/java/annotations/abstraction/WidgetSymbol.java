package annotations.abstraction;

import annotations.abstraction.repeat.WidgetSymbols;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Repeatable(WidgetSymbols.class)
public @interface WidgetSymbol {
    String widgetID();
    String[] events();
    String[] outputIDs();
    String[] params() default {""};
}
