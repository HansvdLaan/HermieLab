package settings.transformers;

import parametergenerators.JavaFXParameterGenerators;
import predicates.JavaFXPredicates;
import predicates.UtilsPredicates;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hans on 31-1-2018.
 */
public class PredicateTransformer {

    private Map<String,Map<String,Object>> generalPredicates = new HashMap<>();

    public static Map<String, Object> find(String id){
        return getInstance().generalPredicates.get(id);
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
