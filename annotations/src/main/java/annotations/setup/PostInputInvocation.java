package annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface PostInputInvocation {
    int order(); //The order of the post input invocations
    String[] params();
}
