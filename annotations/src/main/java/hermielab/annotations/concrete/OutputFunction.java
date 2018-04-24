package hermielab.annotations.concrete;

import hermielab.annotations.concrete.repeat.OutputFunctions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Repeatable(OutputFunctions.class)
public @interface OutputFunction {
    String ID(); //The ID of this Output Function
    String[] parameters() default {};
    String[] predicates() default {};
    String[] nfapredicates() default {};
}
