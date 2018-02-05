package settings.transformers;

import settings.utils.ClassUtils;
import settings.JavaFXPredicates;
import settings.UtilsPredicates;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;

public class PredicateTransformer {

    private Map<String,Map<String,Object>> generalPredicates = new HashMap<>();

    public static Map<String, Object> getData(String id){
        return getInstance().generalPredicates.get(id);
    }

    public static ExecutableElement getMethod(String id) throws ClassNotFoundException, NoSuchMethodException {
        Map<String,Object> data = getData(id);
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
