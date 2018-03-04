package annotations.concrete;

import annotations.concrete.repeat.InputWidgets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Repeatable(InputWidgets.class)
public @interface InputWidget {
    String inputWidgetID();
    String[] events();
    //String[] outputFunctionIDs(); //TODO: when only one is given, this will count for all events!
    //String[] predicateFunctionIDs(); //The ids of the predicates which must be true before this function can be excecuted //TODO: event specific predicates?
    String[] params() default {""};
}
