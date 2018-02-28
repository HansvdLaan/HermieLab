package settings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaFXNFAs {

    private static JavaFXNFAs ourInstance = new JavaFXNFAs();

    public static Map<String,String> getMappings(){
        Map<String,String> mappings = new HashMap<>();
        List<String> events = Arrays.asList("mouse_press","mouse_release","mouse_drag");
        List<String> edgeIDs = Arrays.asList("0","1","2");
        for (int i = 0; i < events.size(); i++){
            mappings.put(events.get(i), edgeIDs.get(i));
        }
        return mappings;
    }

    public static String getEdgeID(String event){
        return getMappings().get(event);
    }

    public static JavaFXNFAs getInstance() {
        return ourInstance;
    }

    private JavaFXNFAs() {
    }
}
