package hermielab.generator.settings.transformers;

import hermielab.generator.utils.ClassUtils;
import hermielab.generator.settings.JavaFXPredicates;
import hermielab.generator.settings.UtilsPredicates;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;

public class PredicateTransformer {

    private Map<String,Map<String,Object>> generalPredicates = new HashMap<>();

    public static Map<String, Object> getMethodData(String id){
        Map<String, Object> methodData = getInstance().generalPredicates.get(id);
        if (methodData == null){
            throw new IllegalArgumentException("unkown library method: " + id);
        } else {
            return getInstance().generalPredicates.get(id);
        }
    }

    public static ExecutableElement getMethod(String id) throws ClassNotFoundException, NoSuchMethodException {
        Map<String,Object> data = getMethodData(id);
        TypeElement clazz = ClassUtils.getClass(data.get("class").toString());
        return ClassUtils.getMethod(clazz,data.get("method").toString());
    }

    public static void addParameterGenerator(String prefix, String id, Map<String,Object> parameters){
        getInstance().generalPredicates.put(prefix + ":" + id, parameters);
    }

    private static PredicateTransformer ourInstance = new PredicateTransformer();

    public static PredicateTransformer getInstance() {
        return ourInstance;
    }

    private PredicateTransformer() {
        generalPredicates.putAll(JavaFXPredicates.getMappings());
        generalPredicates.putAll(UtilsPredicates.getMappings());
    }
}
