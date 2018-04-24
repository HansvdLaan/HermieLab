package hermielab.annotations.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface Parameter {
    String ID();
}
