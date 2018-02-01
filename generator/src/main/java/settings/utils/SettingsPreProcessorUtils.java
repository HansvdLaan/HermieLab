package settings;

import package2.Settings;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Hans on 19-1-2018.
 */
public class SettingsPreProcessorUtils {

    private ProcessingEnvironment prEnv;
    private static SettingsPreProcessorUtils ourInstance = new SettingsPreProcessorUtils();

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
               return SettingsPreProcessorUtils.getInstance().getPrEnv().getElementUtils().getTypeElement(className);
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
        assert methods.size() == 1;
        //TODO: take into account argument!
        System.out.println("METHODS" + methods.toString());
        return methods.get(0);
    }

    public static ExecutableElement getMethod(TypeElement c, VariableElement v, String methodName) throws NoSuchMethodException, ClassNotFoundException {
        TypeElement fieldClass = getClass(v.asType().toString());
        List<ExecutableElement> methods = ElementFilter.methodsIn(getInstance().getPrEnv().getElementUtils().getAllMembers(fieldClass));
        //System.out.println("Methods:" + methods + " - should contain:" + methodName);

        methods = methods.stream().filter(method -> method.toString().equals(methodName)).collect(Collectors.toList());
        //System.out.println("Filtered methods:" + methods + " - should countain:" + methodName);
        assert methods.size() == 1;
        return methods.get(0);
    }

    public static SettingsPreProcessorUtils getInstance() {
        return ourInstance;
    }

    private SettingsPreProcessorUtils() {
    }

    public ProcessingEnvironment getPrEnv() {
        return prEnv;
    }

    public void setPrEnv(ProcessingEnvironment prEnv) {
        this.prEnv = prEnv;
    }
}
