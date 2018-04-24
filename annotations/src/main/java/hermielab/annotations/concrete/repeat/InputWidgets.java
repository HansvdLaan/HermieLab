package hermielab.annotations.concrete.repeat;

import hermielab.annotations.concrete.InputWidget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface InputWidgets {
    InputWidget[] value();
}
