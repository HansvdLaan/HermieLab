package hermielab.annotations.util.repeat;

import hermielab.annotations.util.Predicate;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Predicates {
    Predicate[] value();
}
