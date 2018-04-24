package hermielab.generator.files.mappings;

import org.dom4j.Element;
import hermielab.generator.settings.Settings;
import hermielab.generator.settings.containers.GeneratorInformationElement;

import java.util.Map;
import java.util.function.BiFunction;

public class MappingsTransformations {

    public static BiFunction<Settings,Element,Element> abstractSymbolTransformation = (settings, root) -> {
        Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType("abstractsymbolmapping");
        for (String id : elementMap.keySet()) {
            Element abstractSymbolElem = root.addElement("mapping");
            GeneratorInformationElement element = elementMap.get(id);
            abstractSymbolElem.addElement("ai").addText(element.getID());
            abstractSymbolElem.addElement("ci").addText(element.getAttribute("concreteinput").get(0).toString());
            abstractSymbolElem.addElement("co").addText(element.getAttribute("concreteoutput").get(0).toString());
            abstractSymbolElem.addElement("ao").addText(element.getAttribute("transform").get(0).toString());
        }
        return root;
    };



}
