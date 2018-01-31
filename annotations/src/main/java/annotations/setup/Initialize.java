package annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Initialize {
    int order(); //The order of the initialze function invocations
    String[] params();
}
