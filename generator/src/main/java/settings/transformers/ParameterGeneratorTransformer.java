package settings.transformers;

import annotations.util.ParameterGenerator;
import settings.utils.ClassUtils;
import settings.JavaFXParameterGenerators;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.*;

public class ParameterGeneratorTransformer {

    public static Set<String> SUPPORTEDPRIMITIVES = new HashSet<String>(
            Arrays.asList("string","byte","char","short","int","integer","long","float","double","boolean","bool"));

    private Map<String, Map<String, Object>> generalParameterGenerators = new HashMap<>();

    public static Map<String, Object> getData(String id) {
        return getInstance().generalParameterGenerators.get(id);
    }

    public static ExecutableElement getMethod(String id) throws ClassNotFoundException, NoSuchMethodException {
        Map<String, Object> data = getInstance().generalParameterGenerators.get(id);
        if (data == null) {
            data = getData(id);
        }
        TypeElement clazz = ClassUtils.getClass((String) data.get("class"));
        return ClassUtils.getMethod(clazz, data.get("method").toString());
    }


    public static Object getValue(String type, String value){
        switch (type) {
            case "string":
                return value;
            case "byte":
                return Byte.valueOf(value);
            case "char":
                return value.charAt(0);
            case "short":
                return Short.valueOf(value);
            case "int":
            case "integer":
                return Integer.valueOf(value);
            case "long":
                return Long.valueOf(value);
            case "float":
                return Float.valueOf(value);
            case "double":
                return Double.valueOf(value);
            case "boolean":
            case "bool":
                return Boolean.valueOf(value);
            default:
                throw new IllegalStateException("Unkown primitive type:" + type);
        }
    }

    public static void addParameterGenerator(String prefix, String id, Map<String, Object> parameters) {
        getInstance().generalParameterGenerators.put(prefix + ":" + id, parameters);
    }

    private static ParameterGeneratorTransformer ourInstance = new ParameterGeneratorTransformer();

    public static ParameterGeneratorTransformer getInstance() {
        return ourInstance;
    }

    private ParameterGeneratorTransformer() {
        generalParameterGenerators.putAll(JavaFXParameterGenerators.getInstance().getMappings());
    }

}
