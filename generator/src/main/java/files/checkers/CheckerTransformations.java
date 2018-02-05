package files.checkers;

import org.dom4j.Element;
import settings.Settings;
import settings.containers.GeneratorInformationElement;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CheckerTransformations {

    public static BiFunction<Settings,Element,Element> nfaCheckerTransformation = (settings, root) -> {
        Element nfaCheckerElem = root.addElement("nfachecker");
        Map<String, Map<String,Map<String,String>>> NFAs = new HashMap<>();

        Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType("inputfunction");
        for (String id : elementMap.keySet()) {
            GeneratorInformationElement element = elementMap.get(id);
            Map<String, Object> nfaPredicates = element.getDataSubset("nfapredicate(#[0-9]*)?");
            nfaPredicates.values().stream().forEach(predicate -> {
                String[] parsedPredicate = parseNFAPredicate((String) predicate);
                String groupID = parsedPredicate[3];
                String nfaName = parsedPredicate[0];
                String fileName = parsedPredicate[1];
                String originalEdge = parsedPredicate[2];

                NFAs.putIfAbsent(groupID, new HashMap<>());
                NFAs.get(groupID).putIfAbsent(nfaName, new HashMap<>());
                NFAs.get(groupID).get(nfaName).put("name",nfaName);
                NFAs.get(groupID).get(nfaName).put("file",fileName + ".jff");
                long transitionCount = NFAs.get(groupID).get(nfaName).keySet().stream().filter(key -> key.matches("transition(#[0-9]*)?")).count();
                NFAs.get(groupID).get(nfaName).put("transition#" + transitionCount+1, originalEdge + "," + id);

            });
        }

        for (String group : NFAs.keySet()) {
            Element groupElem = nfaCheckerElem.addElement("group");
            groupElem.addElement("id").addText(group);
            for (String nfaName : NFAs.get(group).keySet()) {
                Element nfaElement = groupElem.addElement("nfa");
                nfaElement.addElement("name").addText(nfaName);
                nfaElement.addElement("file").addText(NFAs.get(group).get(nfaName).get("file"));
                List<String> transitions = NFAs.get(group).get(nfaName).keySet().stream().filter(key -> key.matches("transition(#[0-9]*)?")).collect(Collectors.toList());
                Element transitionElem = nfaElement.addElement("transitions");
                for (String transition: transitions) {
                    transitionElem.addElement("transition").addText(NFAs.get(group).get(nfaName).get(transition));
                }
            }
        }
        return root;
    };

    public static BiFunction<Settings,Element,Element> predicateCheckerTransformation = (settings, root) -> {
        Element nfaCheckerElem = root.addElement("predicatechecker");
        Map<String, Collection<String>> predicates = new HashMap<>();

        Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType("inputfunction");
        for (String id : elementMap.keySet()) {
            GeneratorInformationElement element = elementMap.get(id);
            predicates.put(id,element.getDataSubset("predicate(#[0-9]*)?").values()
                    .stream().map(v -> v.toString()).collect(Collectors.toList()));
        }

        for (String inputID : predicates.keySet()) {
            Element inputElem = nfaCheckerElem.addElement("input");
            inputElem.addElement("id").addText(inputID);
            for (String predicate : predicates.get(inputID)) {
                inputElem.addElement("predicate").addText(predicate);
            }
        }

        return root;
    };

    private static String[] parseNFAPredicate(String NFAPredicate){
        String[] parsedNFAPredicate = new String[4];
        String[] parts = NFAPredicate.split("#");

        parsedNFAPredicate[0] = parts[0];
        parsedNFAPredicate[1] = parts[1].replaceAll("\\[.*\\]","");
        parsedNFAPredicate[2] = parts[1].split("\\[|]")[1];
        parsedNFAPredicate[3] = parts[2];

        return parsedNFAPredicate;

    }

}
