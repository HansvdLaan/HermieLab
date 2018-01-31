package utils.predicates;

import javafx.scene.Node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaFXPredicates {

    private static JavaFXPredicates ourInstance = new JavaFXPredicates();

    public static Map<String,Map<String,Object>> getMappings(){
        Map<String,Map<String,Object>> mappings = new HashMap<>();
        List<String> IDs = Arrays.asList("javaFX:isVisible","javaFX:onScreen","javaFX:isDisabled","javaFX:isAccessible");
        List<String> methods = Arrays.asList("isVisible(javafx.scene.Node)","onScreen(javafx.scene.Node)","isDisabled(javafx.scene.Node)","isAccessible(javafx.scene.Node)");
        for (int i = 0; i < IDs.size(); i++){
            String ID = IDs.get(i);
            String method = methods.get(i);
            mappings.put(ID, new HashMap<>());
            mappings.get(ID).put("method",method);
            mappings.get(ID).put("class","utils.predicates.JavaFXPredicates");
        }
        return mappings;
    }

    public static boolean isVisible(Node node) {
        return node.isVisible();
    }

    public static boolean onScreen(Node node) {
        return node.getScene() == null;
    }

    public static boolean isDisabled(Node node) {
        return node.isDisabled();
    }

    public static boolean isAccessbile(Node node){
        return isVisible(node) && onScreen(node) && isDisabled(node);
    }

    public static JavaFXPredicates getInstance() {
        return ourInstance;
    }

    private JavaFXPredicates() {
    }
}
