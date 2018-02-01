package settings.transformers;

import parametergenerators.JavaFXParameterGenerators;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hans on 31-1-2018.
 */
public class ParameterGeneratorTransformer {

    private Map<String,Map<String,Object>> generalParameterGenerators = new HashMap<>();

    public static Map<String, Object> find(String id){
        return getInstance().generalParameterGenerators.get(id);
    }

    public static void addParameterGenerator(String prefix, String id, Map<String,Object> parameters){
        getInstance().generalParameterGenerators.put(prefix + ":" + id, parameters);
    }

    private static ParameterGeneratorTransformer ourInstance = new ParameterGeneratorTransformer();

    public static ParameterGeneratorTransformer getInstance() {
        return ourInstance;
    }

    private ParameterGeneratorTransformer() {
        generalParameterGenerators.putAll(JavaFXParameterGenerators.getMappings());
    }
}
