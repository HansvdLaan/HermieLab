package hermielab.generator.settings.transformations;

import hermielab.generator.settings.Settings;
import hermielab.generator.settings.containers.GeneratorInformationElement;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;


public class SetupElementsTransformations {

    //X : Initialize -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessStart = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("start",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    //X : Initialize -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessInitialize = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("initialize",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    //X : Initialize -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessShutdown = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("shutdown",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    //X : PreQuery -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessPreQuery = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("prequery",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    //X : PostQuery  -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessPostQuery = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("postquery",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    //X : PreInputInvocation -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessPreInputInvocation = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("preinputinvocation",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    //X : PostInputInvocation -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessPostInputInvocation = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("postinputinvocation",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    //X : PostOutputInvocation -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessPreOutputInvocation = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("preoutputinvocation",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    //X : PostOutputInvocation -> Self
    public static BiFunction<Settings,Settings,TransformationResult> ProcessPostOutputInvocation = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("postoutputinvocation",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
            }
        }
        return new TransformationResult(settings, newElements);
    };
}
