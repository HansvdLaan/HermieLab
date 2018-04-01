package settings;

import settings.containers.GeneratorInformationElement;
import settings.transformations.TransformationResult;

import java.util.function.BiFunction;

/**
 * Created by Hans on 26-2-2018.
 */
public class TransformationTestHelperFunctions {

    public static GeneratorInformationElement generateElement(String type, String id, Object... attributes){
        GeneratorInformationElement element = new GeneratorInformationElement(type, id);
        for (int i = 0; i < attributes.length; i += 2){
            String attributeType = String.valueOf(attributes[i]);
            element.addAttribute(attributeType, attributes[i+1]);
        }
        return element;
    }

    public static boolean containsElements(Settings settings, String type){
        return settings.getSettingsByType(type) != null;
    }

    public static boolean containsElement(Settings settings, String type){
        return settings.getSettingsByType(type) != null;
    }

    public static boolean containsElement(Settings settings, GeneratorInformationElement element) {
        if (element == null){
            throw new IllegalArgumentException("element cannot be null");
        } else {
            return settings.getSettingsByTypeAndID(element.getType(), element.getID()).equals(element);
        }
    }

    public static boolean containsElements(Settings settings, GeneratorInformationElement... elements){
        boolean result = elements != null;
        for (GeneratorInformationElement element: elements){
            result = result && containsElement(settings, element);
        }
        return result;
    }

    public static boolean containsElement(Settings settings, String type, String ID){
        return settings.getSettingsByTypeAndID(type, ID) != null;
    }

    public static boolean containsElements(Settings settings, String type, int amount){
        return settings.getSettingsByType(type).size() == amount;
    }

    public static boolean containsElements(Settings settings, String type, String... IDs){
        boolean result = true;
        for (String ID: IDs) {
            result = result && containsElements(settings, type, ID);
        }
        return result;
    }

    public static boolean containsReferences(Settings settings, String type, int amount){
        return settings.getSettingsByType(type) == null;
    }

    public static boolean containsReference(Settings settings, String type, String ID){
        return settings.getSettingsByTypeAndID(type, ID) == null;
    }

    public static boolean containsReferences(Settings settings, String type, String... IDs){
        boolean result = true;
        for (String ID: IDs) {
            result = result && containsElements(settings, type, ID);
        }
        return result;
    }

    public static boolean containsVariable(GeneratorInformationElement element, String variableID){
        return element.getAttributes().containsKey(variableID);
    }

    public static boolean checkVariableValue(GeneratorInformationElement element, String variableID, Object value){
        return element.getAttribute(variableID).equals(value);
    }

    public static Settings excecuteTransformation(Settings settings, BiFunction<Settings,Settings,TransformationResult>... transformations) {
        SettingsPreProcessor processor = new SettingsPreProcessor(settings);
        for (BiFunction transformation : transformations) {
            processor.addTransformation(transformation);
        }
        processor.applyTransformations();
        return processor.getSettings();
    }


}
