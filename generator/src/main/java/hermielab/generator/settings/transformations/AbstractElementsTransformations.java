package hermielab.generator.settings.transformations;

import hermielab.generator.settings.Settings;
import hermielab.generator.settings.containers.GeneratorInformationElement;

import java.util.*;
import java.util.function.BiFunction;

public class AbstractElementsTransformations {

    // 1 : Widget Symbol -> Self, Input Widget, Reference: {Output Function}
    public static BiFunction<Settings,Settings,TransformationResult> ProcessWidgetSymbols = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("widgetsymbol",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
                HelperFunctions.processOutputFunctionReferences(settings, element);
                Set<GeneratorInformationElement> inputWidgets = generateInputWidgetsFromWidgetSymbol(settings, element).getGeneratedElements();
                inputWidgets.forEach(inputWidget -> {
                    HelperFunctions.addElement(settings, inputWidget);
                    newElements.add(inputWidget);
                });
            }
        }
        return new TransformationResult(settings, newElements);
    };

    // 2 : Field Method Symbol-> Self, Input Field Method, Reference: {Output Function}
    public static BiFunction<Settings,Settings,TransformationResult> ProcessFieldMethodSymbols = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("fieldmethodsymbol",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
                HelperFunctions.processOutputFunctionReferences(settings, element);
                Set<GeneratorInformationElement> inputWidgets = generateInputFieldMethodsFromFieldMethodSymbol(settings, element).getGeneratedElements();
                inputWidgets.forEach(inputWidget -> {
                    HelperFunctions.addElement(settings, inputWidget);
                    newElements.add(inputWidget);
                });
            }
        }
        return new TransformationResult(settings, newElements);
    };

    // 3 : Function Symbol-> Self, Input Function, Reference: {Output Function}
    public static BiFunction<Settings,Settings,TransformationResult> ProcessFunctionSymbols = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("functionsymbol",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
                HelperFunctions.processOutputFunctionReferences(settings, element);
                Set<GeneratorInformationElement> inputWidgets = generateInputFunctionsFromFunctionSymbol(settings, element).getGeneratedElements();
                inputWidgets.forEach(inputWidget -> {
                    HelperFunctions.addElement(settings, inputWidget);
                    newElements.add(inputWidget);
                });
            }
        }
        return new TransformationResult(settings, newElements);
    };

    // X : Widget Symbol -> Input Widget
    public static TransformationResult generateInputWidgetsFromWidgetSymbol(Settings settings, GeneratorInformationElement element){
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        GeneratorInformationElement newElement = new GeneratorInformationElement("inputwidget",element.getID(),
                element,
                element.getAttributes());
        newElement.removeAttribute("output");
        newElements.add(newElement);
        return new TransformationResult(settings, newElements);
    }

    // X : Field Method Symbol -> Input Widget
    public static TransformationResult generateInputFieldMethodsFromFieldMethodSymbol(Settings settings, GeneratorInformationElement element){
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        GeneratorInformationElement newElement = new GeneratorInformationElement("inputfieldmethod",element.getID(),
                element,
                element.getAttributes());
        newElement.removeAttribute("output");
        newElements.add(newElement);
        return new TransformationResult(settings, newElements);
    }

    // X : Function Symbol -> Input Function
    public static TransformationResult generateInputFunctionsFromFunctionSymbol(Settings settings, GeneratorInformationElement element){
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        GeneratorInformationElement newElement = new GeneratorInformationElement("inputfunction",element.getID(),
                element,
                element.getAttributes());
        newElement.removeAttribute("output");
        newElements.add(newElement);
        return new TransformationResult(settings, newElements);
    }

    //Last : Symbol -> Abstract Symbol Mappings
    public static BiFunction<Settings,Settings,TransformationResult> ProcessSymbolsToGenerateMappings = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        Map<String, GeneratorInformationElement> widgetSymbols = new HashMap<>();
        widgetSymbols.putAll(settings.getSettingsByType("widgetsymbol"));
        for (GeneratorInformationElement widgetSymbol: widgetSymbols.values()){
            TransformationResult result = generateAbstractSymbolMappingFromWidgetSymbol(settings, widgetSymbol);
            result.getGeneratedElements().forEach(mapping -> {
                newElements.add(mapping);
                HelperFunctions.addElement(settings,mapping);
            });
        }
        Map<String, GeneratorInformationElement> fieldMethodSymbols = new HashMap<>();
        fieldMethodSymbols.putAll(settings.getSettingsByType("fieldmethodsymbol"));
        for (GeneratorInformationElement fieldMethodSymbol: fieldMethodSymbols.values()){
            TransformationResult result = generateAbstractSymbolMappingFromFieldMethodSymbol(settings, fieldMethodSymbol);
            result.getGeneratedElements().forEach(mapping -> {
                newElements.add(mapping);
                HelperFunctions.addElement(settings,mapping);
            });
        }
        Map<String, GeneratorInformationElement> functionSymbols = new HashMap<>();
        functionSymbols.putAll(settings.getSettingsByType("functionsymbol"));
        for (GeneratorInformationElement functionSymbol: functionSymbols.values()){
            TransformationResult result = generateAbstractSymbolMappingFromFunctionSymbol(settings, functionSymbol);
            result.getGeneratedElements().forEach(mapping -> {
                newElements.add(mapping);
                HelperFunctions.addElement(settings,mapping);
            });
        }
        return new TransformationResult(settings, newElements);
    };

    public static TransformationResult generateAbstractSymbolMappingFromWidgetSymbol(Settings settings, GeneratorInformationElement WidgetSymbol){
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        List<GeneratorInformationElement> descendants = new LinkedList<>(HelperFunctions.getDescendants(settings,WidgetSymbol));
        descendants.removeIf(descendant -> !descendant.getType().equals("inputfunction"));
        Collections.sort(descendants);

        int counter = 1;
        for (GeneratorInformationElement concreteInputFunction: descendants) {
            String abstractInputID;
            if (descendants.size() > 1) {
                abstractInputID = WidgetSymbol.getID() + "_Symbol" + counter;
            } else {
                abstractInputID = WidgetSymbol.getID() + "_Symbol";
            }
            String concreteInputFunctionID = concreteInputFunction.getID();
            String concreteOutputFunctionID = WidgetSymbol.getStringAttribute("output").get(0);
            String transformFunctionID = "toString";
            newElements.addAll(generateAbstractSymbolMapping(settings, WidgetSymbol,
                    abstractInputID, concreteInputFunctionID, concreteOutputFunctionID, transformFunctionID ).getGeneratedElements());
            counter++;
        }

        return new TransformationResult(settings, newElements);
    }

    public static TransformationResult generateAbstractSymbolMappingFromFieldMethodSymbol(Settings settings, GeneratorInformationElement fieldMethodSymbol){
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        List<GeneratorInformationElement> descendants = new LinkedList<>(HelperFunctions.getDescendants(settings,fieldMethodSymbol));
        descendants.removeIf(descendant -> !descendant.getType().equals("inputfunction"));
        Collections.sort(descendants);

        int counter = 1;
        for (GeneratorInformationElement concreteInputFunction: descendants) {
            String abstractInputID;
            if (descendants.size() > 1) {
                abstractInputID = fieldMethodSymbol.getID() + "_Symbol" + counter;
            } else {
                abstractInputID = fieldMethodSymbol.getID() + "_Symbol";
            }
            String concreteInputFunctionID = concreteInputFunction.getID();
            String concreteOutputFunctionID = fieldMethodSymbol.getStringAttribute("output").get(0);
            String transformFunctionID = "toString";
            newElements.addAll(generateAbstractSymbolMapping(settings, fieldMethodSymbol,
                    abstractInputID, concreteInputFunctionID, concreteOutputFunctionID, transformFunctionID ).getGeneratedElements());
            counter++;
        };

        return new TransformationResult(settings, newElements);
    }

    public static TransformationResult generateAbstractSymbolMappingFromFunctionSymbol(Settings settings, GeneratorInformationElement functionSymbol){
        Set<GeneratorInformationElement> descendants = HelperFunctions.getDescendants(settings,functionSymbol);
        descendants.removeIf(descendant -> !descendant.getType().equals("inputfunction"));
        GeneratorInformationElement concreteInputFunction = (GeneratorInformationElement) descendants.toArray()[0];
        String concreteInputFunctionID = concreteInputFunction.getID();
        String concreteOutputFunctionID = functionSymbol.getStringAttribute("output").get(0);
        String transformFunctionID = "toString";

        return generateAbstractSymbolMapping(settings, functionSymbol,
                functionSymbol.getID(), concreteInputFunctionID, concreteOutputFunctionID, transformFunctionID);
    }

    // X : Any Symbol -> Input Function
    public static TransformationResult generateAbstractSymbolMapping(Settings settings, GeneratorInformationElement symbol, String abstractInputID, String concreteInputFunctionID,
                                                                     String concreteOutputFunctionID, String transformFunctionID){
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        newElements.add(new GeneratorInformationElement("abstractsymbolmapping",abstractInputID, symbol,
                "concreteinput",concreteInputFunctionID,
                "concreteoutput",concreteOutputFunctionID,
                "transform", transformFunctionID));
        return new TransformationResult(settings, newElements);
    }

}
