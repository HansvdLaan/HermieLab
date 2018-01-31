package annotations.concrete;

import annotations.repeat.OutputFieldMethods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Repeatable(OutputFieldMethods.class)
public @interface OutputFieldMethod {
    String id();
    String fieldMethod(); //The id of this output Function, "the input letter of the input alphabet of the learner"
    String[] params();
}
