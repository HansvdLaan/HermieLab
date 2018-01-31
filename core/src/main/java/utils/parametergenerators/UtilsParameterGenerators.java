package utils.parametergenerators;

import java.util.HashMap;
import java.util.Map;

public class UtilsParameterGenerators {

    private static UtilsParameterGenerators ourInstance = new UtilsParameterGenerators();

    public static Map<String,Map<String,Object>> getMappings(){
        Map<String,Map<String,Object>> mappings = new HashMap<>();
        return mappings;
    }



    public static UtilsParameterGenerators getInstance() {
        return ourInstance;
    }

    private UtilsParameterGenerators() {
    }

}
