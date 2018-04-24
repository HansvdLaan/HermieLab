package hermielab.annotations.concrete;

import hermielab.annotations.concrete.repeat.InputWidgets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Repeatable(InputWidgets.class)
public @interface InputWidget {
    String widgetID();
    String[] events();
    String[] parameters() default {};
    String[] predicates() default {};
    String[] nfapredicates() default {};
}
