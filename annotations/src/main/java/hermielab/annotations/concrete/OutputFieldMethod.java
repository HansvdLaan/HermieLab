package hermielab.annotations.concrete;

import hermielab.annotations.concrete.repeat.OutputFieldMethods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Repeatable(OutputFieldMethods.class)
public @interface OutputFieldMethod {
    String ID();
    String fieldMethod();
    String[] parameters() default {};
    String[] predicates() default {};
    String[] nfapredicates() default {};
}
