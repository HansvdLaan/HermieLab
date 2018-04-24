package hermielab.generator.utils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassUtils {

    private ProcessingEnvironment prEnv;
    private static ClassUtils ourInstance = new ClassUtils();
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static TypeElement getClass(String className) throws ClassNotFoundException {
        switch (className) {
            case "bool":
                return getClass("java.lang.Boolean");
            case "byte":
                return getClass("java.lang.Byte");
            case "short":
                return getClass("java.lang.Short");
            case "int":
                return getClass("java.lang.Integer");
            case "long":
                return getClass("java.lang.Long");
            case "float":
                return getClass("java.lang.Float");
            case "double":
                return getClass("java.lang.Double");
            case "char":
                return getClass("java.lang.Character");
            case "void":
                return getClass("java.lang.Void");
            default:
               return ClassUtils.getInstance().getPrEnv().getElementUtils().getTypeElement(className);
        }
    }

    public static VariableElement getField(TypeElement clazz, String fieldName) throws ClassNotFoundException {
        List<VariableElement> fields = clazz.getEnclosedElements().stream().filter(elem -> elem instanceof VariableElement)
                .map(elem -> (VariableElement) elem)
                .filter(field -> field.toString().equals(fieldName))
                .collect(Collectors.toList());
        assert fields.size() == 1;
        return fields.get(0);
    }

    public static ExecutableElement getMethod(TypeElement c, String methodName) throws NoSuchMethodException {
        List<? extends Element> elements = c.getEnclosedElements();
        List<ExecutableElement> methods = new LinkedList<>();
        for (Element element: elements){
            if (element instanceof ExecutableElement){
                methods.add( (ExecutableElement) element);
            }
        }
        //System.out.println("method name: " + methodName);
        methods = methods.stream().filter(method -> method.toString().equals(methodName)).collect(Collectors.toList());

        if (methods.size() == 0) {
            throw new IllegalArgumentException("Cannot find the method:" + methodName + " for " + c.toString());
        }
        if (methods.size() > 1) {
            throw new IllegalArgumentException("More than one method found:" + methodName + " for " + c.toString());
        }
        //TODO: take into account argument!
        return methods.get(0);
    }

    public static ExecutableElement getMethod(TypeElement c, VariableElement v, String methodName) throws NoSuchMethodException, ClassNotFoundException {
        TypeElement fieldClass = getClass(v.asType().toString());
        List<ExecutableElement> methods = ElementFilter.methodsIn(getInstance().getPrEnv().getElementUtils().getAllMembers(fieldClass));

        methods = methods.stream().filter(method -> method.toString().equals(methodName)).collect(Collectors.toList());

        if (methods.size() == 0) {
            throw new IllegalArgumentException("Cannot find the method:" + methodName + " for " + c.toString() + "." + v.toString());
        }
        if (methods.size() > 1) {
            throw new IllegalArgumentException("More than one method found:" + methodName + " for " + c.toString() + "." + v.toString());
        }
        return methods.get(0);
    }

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }

    public static ClassUtils getInstance() {
        return ourInstance;
    }

    private ClassUtils() {
    }

    public ProcessingEnvironment getPrEnv() {
        return prEnv;
    }

    public void setPrEnv(ProcessingEnvironment prEnv) {
        this.prEnv = prEnv;
    }





}
