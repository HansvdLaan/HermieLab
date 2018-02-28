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
            Map<String, Object> nfaPredicates = element.getAttributeSubset("nfapredicate(#[0-9]*)?");
            nfaPredicates.values().stream().forEach(predicate -> {
                String[] parsedPredicate = parseNFAPredicate((String) predicate); //NFANAME#FILENAME[ORIGINALEDGE]#GROUPID#INTERGRATIONPREDICATES#DESINTERGRATIONPREDICATES
                String nfaName = parsedPredicate[0];
                String fileName = parsedPredicate[1];
                String originalEdge = parsedPredicate[2];
                String groupID = parsedPredicate[3];
                NFAs.putIfAbsent(groupID, new HashMap<>());
                NFAs.get(groupID).putIfAbsent(nfaName, new HashMap<>());
                NFAs.get(groupID).get(nfaName).put("name",nfaName);
                NFAs.get(groupID).get(nfaName).put("file",fileName + ".jff");
                long transitionCount = NFAs.get(groupID).get(nfaName).keySet().stream().filter(key -> key.matches("transition(#[0-9]*)?")).count();
                NFAs.get(groupID).get(nfaName).put("transition#" + transitionCount+1, originalEdge + "," + id);

                if (parsedPredicate.length > 4){
                    String[] intergrationPredicates = parsedPredicate[4].split(",");
                    long intergrationPredicateCount = NFAs.get(groupID).get(nfaName).keySet().stream().filter(key -> key.matches("intergration(#[0-9]*)?")).count();
                    for (String intergrationPredicate: intergrationPredicates){
                        if (!intergrationPredicate.equals("_")) {
                            NFAs.get(groupID).get(nfaName).put("intergration#" + intergrationPredicateCount + 1, intergrationPredicate);
                            intergrationPredicateCount++;
                        }
                    }
                }

                if (parsedPredicate.length > 5){
                    String[] deintergrationPredicates = parsedPredicate[5].split(",");
                    long deintergrationPredicateCount = NFAs.get(groupID).get(nfaName).keySet().stream().filter(key -> key.matches("deintergration(#[0-9]*)?")).count();
                    for (String deintergrationPredicate: deintergrationPredicates){
                        if (!deintergrationPredicate.equals("_")) {
                            NFAs.get(groupID).get(nfaName).put("deintergration#" + deintergrationPredicateCount + 1, deintergrationPredicate);
                            deintergrationPredicateCount++;
                        }
                    }
                }
            });
        }

        for (String group : NFAs.keySet()) {
            Element groupElem = nfaCheckerElem.addElement("group");
            groupElem.addElement("id").addText(group);
            for (String nfaName : NFAs.get(group).keySet()) {
                Element nfaElement = groupElem.addElement("nfa");
                nfaElement.addElement("name").addText(nfaName);
                nfaElement.addElement("file").addText(NFAs.get(group).get(nfaName).get("file"));

                List<String> options = Arrays.asList("transition","intergration","deintergration");
                for (String option: options) {
                    List<String> transitions = NFAs.get(group).get(nfaName).keySet().stream().filter(key -> key.matches(option + "(#[0-9]*)?")).collect(Collectors.toList());
                    Element transitionElem = nfaElement.addElement(option + "s");
                    for (String transition : transitions) {
                        transitionElem.addElement(option).addText(NFAs.get(group).get(nfaName).get(transition));
                    }
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
            predicates.put(id,element.getAttributeSubset("predicate(#[0-9]*)?").values()
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
        String[] parts = NFAPredicate.split("#");
        String[] parsedNFAPredicate = new String[parts.length+1];


        parsedNFAPredicate[0] = parts[0];
        parsedNFAPredicate[1] = parts[1].replaceAll("\\[.*\\]","");
        parsedNFAPredicate[2] = parts[1].split("\\[|]")[1];
        parsedNFAPredicate[3] = parts[2];
        if (parts.length > 3) {
            parsedNFAPredicate[4] = parts[3];
        }
        if (parts.length > 4) {
            parsedNFAPredicate[5] = parts[4];
        }

        return parsedNFAPredicate;

    }

}
