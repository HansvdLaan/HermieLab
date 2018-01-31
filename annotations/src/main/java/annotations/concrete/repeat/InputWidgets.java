package annotations.concrete.repeat;

import annotations.concrete.InputWidget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface InputWidgets {
    InputWidget[] value();
}
