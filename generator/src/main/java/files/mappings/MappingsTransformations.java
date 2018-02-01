package files.mappings;

import org.dom4j.Element;
import settings.Settings;
import settings.containers.GeneratorInformationElement;

import java.util.Map;
import java.util.function.BiFunction;

public class MappingsTransformations {

    public static BiFunction<Settings,Element,Element> abstractSymbolTransformation = (settings, root) -> {
        Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType("abstractsymbol");
        for (String id : elementMap.keySet()) {
            Element abstractSymbolElem = root.addElement("abstractsymbol");
            GeneratorInformationElement element = elementMap.get(id);
            abstractSymbolElem.addElement("ai").addText(element.getId());
            abstractSymbolElem.addElement("ci").addText(element.getData().get("input").toString());
            abstractSymbolElem.addElement("co").addText(element.getData().get("output").toString());
            abstractSymbolElem.addElement("ao").addText("null");
        }
        return root;
    };



}
