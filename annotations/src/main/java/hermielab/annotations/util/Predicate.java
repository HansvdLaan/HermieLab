package hermielab.annotations.util;

import hermielab.annotations.util.repeat.Predicates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * The annotation type {@code hermielab.annotations.util.Predicate} is
 * used to create a new predicate which can be referenced in a symbol.
 */
@Target(ElementType.METHOD)
@Repeatable(Predicates.class)
public @interface Predicate {
    /**
     * The ID of the predicate. This is the ID you can reference to in
     * predicate declarations in symbols.
     * @return the costum ID of this predicate
     */
    String ID();
}
