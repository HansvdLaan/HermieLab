package annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Shutdown {
    //String learnerid();
    String[] params();
    int order();
}
