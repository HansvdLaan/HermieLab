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
        Map<String, Collection<String>> NFAs = new HashMap<>();

        Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType("inputfunction");
        for (String id : elementMap.keySet()) {
            GeneratorInformationElement element = elementMap.get(id);
            Map<String, Object> nfaPredicates = element.getDataSubset("nfapredicate(#[0-9]*)?");
            nfaPredicates.values().stream().forEach(predicate -> {
                String[] parsedPredicate = parseNFAPredicate((String) predicate);
                String groupID = parsedPredicate[3];
                String nfaFileName = parsedPredicate[0];
                NFAs.putIfAbsent(groupID, new HashSet<>());
                NFAs.get(groupID).add(nfaFileName);
            });
        }

        for (String group : NFAs.keySet()) {
            Element groupElem = nfaCheckerElem.addElement("group");
            groupElem.addElement("id").addText(group);
            for (String nfaFileName : NFAs.get(group)) {
                groupElem.addElement("nfa").addText(nfaFileName);
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
