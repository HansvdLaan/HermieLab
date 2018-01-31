package annotations.util.repeat;

import annotations.util.Transform;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Transforms {
    Transform[] value();
}
