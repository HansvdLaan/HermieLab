package settings.containers;

import org.dom4j.Node;
import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hans on 31-12-2017.
 */
public class GeneratorInformationElementParser {

    private Map<String,Integer> anonymousIDCounter;

    public GeneratorInformationElementParser(){
        anonymousIDCounter = new HashMap<>();
    }

   public GeneratorInformationElement parse(Node node) {
       String type = node.getName();
       String id = "";
       anonymousIDCounter.putIfAbsent(type, 0);

       List<Node> nodeList = node.selectNodes("*");
       Map<String, Object> data = new HashMap<>();
       for (Node infoNode : nodeList) {
           if (infoNode.getName().equals("id")){
               id = infoNode.getText();
           }
           data.put(infoNode.getName().replace("-","#"), infoNode.getText());
       }

       if (id.equals("")){
           id = type + anonymousIDCounter.get(type);
           anonymousIDCounter.put(type, anonymousIDCounter.get(type) + 1);
       }

       return new GeneratorInformationElement(type, id, data);
   }
}
