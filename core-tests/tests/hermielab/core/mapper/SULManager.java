package hermielab.core.mapper;

import java.util.HashMap;
import java.util.Map;

public class SULManager {

    private Map<String,Object> instances;
    private Map<String, Class> types;

    public Map<String, Object> getInstances() {
        return instances;
    }

    public void setInstance(String id, Object instance) {
        this.getInstances().put(id, instance);
    }

    public Map<String, Class> getTypes() {
        return types;
    }

    public void setType(String id, Class type) {
        this.getInstances().put(id,type);
    }

    private static SULManager ourInstance = new SULManager();

    public static SULManager getInstance() {
        return ourInstance;
    }

    private SULManager() {
        instances = new HashMap<>();
        types= new HashMap<>();
    }
}
