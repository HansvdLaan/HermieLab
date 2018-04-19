package files.experiments;

import org.dom4j.Element;
import settings.Settings;
import settings.containers.GeneratorInformationElement;

import java.util.Map;
import java.util.function.BiFunction;

public class ExperimentTransformations {

    public static BiFunction<Settings,Element,Element> experimentTransformations = (settings, root) -> {
        Map<String, GeneratorInformationElement> startElemMap = settings.getSettingsByType("start");
        int counter = 1;
        for (String startID : startElemMap.keySet()) {
            Element experimentElem = root.addElement("experiment");
            GeneratorInformationElement startElem = startElemMap.get(startID);
            experimentElem.addElement("ID").addText("experiment" + counter);
            experimentElem.addElement("automaton").addText(startElem.getAttribute("automaton").get(0).toString());
            experimentElem.addElement("membershiporacle").addText("TBA");
            experimentElem.addElement("equivilanceoracle").addText("TBA");

            Element alphabet = experimentElem.addElement("alphabet");
            Map<String, GeneratorInformationElement> symbolElemMap = settings.getSettingsByType("abstractsymbolmapping");
            for (String symbolID: symbolElemMap.keySet()){
                GeneratorInformationElement symbolElem = symbolElemMap.get(symbolID);
                if (symbolElem.getAttribute("experiment").size() > 0) {
                    if (symbolElem.getStringAttribute("experiment").stream().anyMatch(v -> v.equals(startID))) {
                        alphabet.addElement("ai").addText(symbolID);
                    }
                } else {
                    alphabet.addElement("ai").addText(symbolID);
                }
            }
            counter++;
        }
        return root;
    };
}
