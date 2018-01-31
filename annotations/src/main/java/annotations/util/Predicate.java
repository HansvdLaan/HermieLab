package annotations.util;

import annotations.util.repeat.Predicates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Repeatable(Predicates.class)
public @interface Predicate {
    String id(); //The id of this Predicate Function
    String[] params();
}
