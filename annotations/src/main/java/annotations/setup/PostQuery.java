package annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface PostQuery {
    int order(); //The order of the post query function invocations
    String[] params();
}
