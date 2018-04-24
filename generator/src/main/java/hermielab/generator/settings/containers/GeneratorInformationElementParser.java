package hermielab.generator.settings.containers;

import org.dom4j.Node;

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
       GeneratorInformationElement element = new GeneratorInformationElement(type, id);
       anonymousIDCounter.putIfAbsent(type, 0);

       List<Node> nodeList = node.selectNodes("*");
       for (Node infoNode : nodeList) {
           if (infoNode.getName().equals("ID")){
               id = infoNode.getText();
               element.setID(id);
           } else {
               String attributeType = infoNode.getName().replaceAll("-.*","");
               if (infoNode.getName().split("-").length == 2) {
                   Integer index = Integer.valueOf(infoNode.getName().split("-")[1]);
                   element.addToListedAttributes(attributeType, infoNode.getText(), index);
               } else {
                   element.addAttribute(attributeType, infoNode.getText());
               }
           }
       }
       if (id.equals("")){
           id = type + anonymousIDCounter.get(type);
           element.setID(id);
           anonymousIDCounter.put(type, anonymousIDCounter.get(type) + 1);
       }
       return element;
   }
}
