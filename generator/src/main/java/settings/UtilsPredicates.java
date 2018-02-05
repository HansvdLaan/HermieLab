package settings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilsPredicates {

    private static UtilsPredicates ourInstance = new UtilsPredicates();

    public static Map<String,Map<String,Object>> getMappings(){
        Map<String,Map<String,Object>> mappings = new HashMap<>();
        List<String> IDs = Arrays.asList("utils:isNull");
        List<String> methods = Arrays.asList("isNull(Object)");
        for (int i = 0; i < IDs.size(); i++){
            String ID = IDs.get(i);
            String method = methods.get(i);
            mappings.put(ID, new HashMap<>());
            mappings.get(ID).put("method",method);
            mappings.get(ID).put("class","settings.JavaFXPredicates");
        }
        return mappings;
    }
    
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static UtilsPredicates getInstance() {
        return ourInstance;
    }

    private UtilsPredicates() {
    }

}
