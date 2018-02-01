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
            experimentElem.addElement("id").addText("experiment" + counter);
            experimentElem.addElement("automaton").addText(startElem.getData().get("automaton").toString());
            experimentElem.addElement("membershiporacle").addText("TBD");
            experimentElem.addElement("equivilanceoracle").addText("TBD");

            Element alphabet = experimentElem.addElement("alphabet");
            Map<String, GeneratorInformationElement> symbolElemMap = settings.getSettingsByType("abstractsymbol");
            for (String symbolID: symbolElemMap.keySet()){
                GeneratorInformationElement symbolElem = symbolElemMap.get(symbolID);
                if (symbolElem.getDataSubset("experiment(#(.*))?").size() > 0) {
                    if (symbolElem.getDataSubset("experiment(#(.*))?").values().stream().anyMatch(v -> v.equals(startID))) {
                        alphabet.addElement("symbol").addText(symbolID);
                    }
                } else {
                    alphabet.addElement("symbol").addText(symbolID);
                }
            }
            counter++;
        }
        return root;
    };
}
