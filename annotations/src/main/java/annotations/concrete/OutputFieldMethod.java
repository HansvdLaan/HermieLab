package annotations.concrete;

import annotations.concrete.repeat.OutputFieldMethods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Repeatable(OutputFieldMethods.class)
public @interface OutputFieldMethod {
    String id();
    String fieldMethod();
    String[] params();
}
