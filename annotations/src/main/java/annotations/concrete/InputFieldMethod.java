package annotations.concrete;

import annotations.concrete.repeat.InputFieldMethods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Repeatable(InputFieldMethods.class)
public @interface InputFieldMethod {
    String inputFieldID();
    String[] fieldMethods(); //The id of this output Function, "the input letter of the input alphabet of the learner"
    //String[] outputFunctionIDs();
    String[] params();
}
