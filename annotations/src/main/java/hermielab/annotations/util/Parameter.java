package hermielab.annotations.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.util.Parameter} is
 * used to create a new parameter which can be referenced in a symbol.
 */
@Target(ElementType.FIELD)
public @interface Parameter {
    /**
     * The ID of the parameter. This is the ID you can reference to in
     * parameter declarations in symbols.
     * @return the costum ID of this parameter
     */
    String ID();
}
