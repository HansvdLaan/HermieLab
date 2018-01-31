package annotations.setup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface PostOutputInvocation {
    int order(); //The order of he post output invocations
    String[] params();
}
