package annotations.util.repeat;

import annotations.util.Predicate;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Predicates {
    Predicate[] value();
}
