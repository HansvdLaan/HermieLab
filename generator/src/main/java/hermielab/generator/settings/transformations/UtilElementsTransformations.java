package hermielab.generator.settings.transformations;

import hermielab.generator.settings.Settings;
import hermielab.generator.settings.containers.GeneratorInformationElement;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public class UtilElementsTransformations {

    //9 : Parameter -> Self, Parameter Generator
    public static BiFunction<Settings,Settings,TransformationResult> ProcessParameters = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("parameter",init,settings).values()) {
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);

                GeneratorInformationElement newElement = new GeneratorInformationElement("parametergenerator", element.getID(),
                        element, element.getSingleAttributes(), element.getOrdenedAttributes());
                element.addChild(newElement);
                HelperFunctions.addElement(settings, element);

                newElements.add(newElement);
                HelperFunctions.addElement(settings, newElement);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    //X : Parameter Generator -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessParameterGenerators = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("parametergenerator",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    //X : Predicate -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessPredicates = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("predicate",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    //X : Transform -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessTransform = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("transform",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };
}
