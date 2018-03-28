package instancemanager;

import java.lang.Class;
import java.lang.Object;
import java.util.HashMap;
import java.util.Map;

public class InstanceManager {

    private static InstanceManager ourInstance = new InstanceManager();

    private Map<Class, Object> classInstances;

    private InstanceManager() {
        classInstances = new HashMap();
    }

    public void addClassInstance(Object c) {
        classInstances.put(c.getClass(),c);
    }

    public Object getClassInstance(Class c) {
        return classInstances.get(c);
    }

    public static InstanceManager getInstance() {
        return ourInstance;
    }
}