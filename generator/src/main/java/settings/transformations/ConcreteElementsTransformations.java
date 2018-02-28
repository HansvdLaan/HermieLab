package settings.transformations;

import settings.JavaFXNFAs;
import settings.Settings;
import settings.containers.GeneratorInformationElement;
import settings.transformations.exceptions.IncompleteElementException;
import settings.transformers.PredicateTransformer;

import java.util.*;
import java.util.function.BiFunction;

public class ConcreteElementsTransformations {

    // 4 : Input Widget -> Self, Input Field, Parameter, Predicates, Reference: {Parameter, Predicate}
    public static BiFunction<Settings,Settings,TransformationResult> ProcessInputWidgets = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement inputWidget: HelperFunctions.combineTypeSettings("inputwidget",init,settings).values()){
            if (inputWidget != null) {
                HelperFunctions.addSelf(settings, inputWidget);
                newElements.add(inputWidget);

                Set<GeneratorInformationElement> fieldParameters = generateParametersFromInputWidgets(settings, inputWidget).getGeneratedElements(); //Should just have one parameter
                GeneratorInformationElement fieldParameter = (GeneratorInformationElement) fieldParameters.toArray()[0];
                HelperFunctions.addElement(settings, fieldParameter);
                newElements.add(fieldParameter);

                Set<GeneratorInformationElement> predicates = generatePredicatesFromInputWidget(settings, inputWidget, fieldParameter).getGeneratedElements();
                predicates.forEach(predicate -> {
                    HelperFunctions.addElement(settings, predicate);
                    newElements.add(predicate);
                });

                Set<GeneratorInformationElement> inputFieldMethods = generateInputFieldsFromInputWidget(settings, inputWidget, new LinkedList<>(predicates)).getGeneratedElements();
                inputFieldMethods.forEach(inputFieldMethod -> {
                    HelperFunctions.addElement(settings, inputFieldMethod);
                    newElements.add(inputFieldMethod);
                });

                HelperFunctions.processParameterGeneratorReferences(settings, inputWidget);
                HelperFunctions.processPredicateReferences(settings, inputWidget);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    // 5 : Input Field Method -> Self, Input Function, Parameter, Predicates, Reference: {Parameter, Predicate}
    public static BiFunction<Settings,Settings,TransformationResult> ProcessInputFieldMethods = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("inputfieldmethod",init,settings).values()){
            if (element != null) {
                Stack<String> parameters = HelperFunctions.getParameterStack(element);
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
                HelperFunctions.processParameterGeneratorReferences(settings, element);
                HelperFunctions.processPredicateReferences(settings, element);
                int methodCounter = 1;
                for(String method: element.getStringAttribute("method")) {
                    GeneratorInformationElement newElement;
                    if (element.getStringAttribute("method").size() == 1) {
                        newElement = new GeneratorInformationElement("inputfunction", element.getID(),
                                element, element.getAttributes());
                    } else {
                        newElement = new GeneratorInformationElement("inputfunction", element.getID() + "_Method" + methodCounter,
                                element, element.getAttributes());
                    }
                    newElement.overwriteAttribute("method", method);
                    newElement.removeAttribute("parameter");
                    for (int i = 0; i < HelperFunctions.determineAmountOfParameters(method); i++) {
                        if (!parameters.isEmpty()) {
                            newElement.addAttribute("parameter", parameters.pop());
                        } else {
                            throw new IncompleteElementException("input field method" + element.getID() + "lacks enough parameters for the generated input functions");
                        }
                    }
                    newElements.add(newElement);
                    HelperFunctions.addElement(settings, newElement);
                    methodCounter++;
                }
                if (!parameters.isEmpty()) {
                    throw new IncompleteElementException("input field method" + element.getID() + "has too many parameters for the generated input functions");
                }
            }
        }
        return new TransformationResult(settings, newElements);
    };

    // 6 : Input Method -> Self, Reference: {Parameter, Predicate}
    public static BiFunction<Settings,Settings,TransformationResult> ProcessInputFunctions = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("inputfunction",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
                HelperFunctions.processParameterGeneratorReferences(settings, element);
                HelperFunctions.processPredicateReferences(settings, element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    // 7 : Output Field Method -> Self, Reference: {Parameter, Predicate}
    public static BiFunction<Settings,Settings,TransformationResult> ProcessOutputFieldMethods = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("outputfieldmethod",init,settings).values()){
            if (element != null) {
                Stack<String> parameters = new Stack<String>();
                List<String> parameterList = element.getStringAttribute("parameter");
                Collections.reverse(parameterList);
                parameters.addAll(parameterList);
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
                HelperFunctions.processParameterGeneratorReferences(settings, element);
                int methodCounter = 1;
                for(String method: element.getStringAttribute("method")) {
                    GeneratorInformationElement newElement = new GeneratorInformationElement("outputfunction", element.getID() + "_Method" + methodCounter,
                            element, element.getAttributes());
                    newElement.overwriteAttribute("method", method);
                    newElement.removeAttribute("parameter");
                    for (int i = 0; i < HelperFunctions.determineAmountOfParameters(method); i++) {
                        if (!parameters.isEmpty()) {
                            newElement.addAttribute("parameter", parameters.pop());
                        } else {
                            throw new IncompleteElementException("output field method" + element.getID() + "lacks enough parameters for the generated output functions");
                        }
                    }
                    newElements.add(newElement);
                    HelperFunctions.addElement(settings, newElement);
                    methodCounter++;
                }
                if (!parameters.isEmpty()) {
                    throw new IncompleteElementException("output field method" + element.getID() + "has too many parameters for the generated output functions");
                }
            }
        }
        return new TransformationResult(settings, newElements);
    };

    // 8 : Input Method -> Self, Reference: {Parameter, Predicate}
    public static BiFunction<Settings,Settings,TransformationResult> ProcessOutputFunctions = (init, settings) -> {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        for(GeneratorInformationElement element: HelperFunctions.combineTypeSettings("outputfunction",init,settings).values()){
            if (element != null) {
                HelperFunctions.addSelf(settings, element);
                newElements.add(element);
                HelperFunctions.processParameterGeneratorReferences(settings, element);
                HelperFunctions.processPredicateReferences(settings, element);
            }
        }
        return new TransformationResult(settings, newElements);
    };

    // X : Input Widget, Parameters, Predicates -> Input Fields
    public static TransformationResult generateInputFieldsFromInputWidget(Settings settings, GeneratorInformationElement inputWidget,
                                                                          List<GeneratorInformationElement> predicates) {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        boolean isJavaFX = isJavaFXWidget(inputWidget);
        boolean isSwing = isSwingWidget(inputWidget);
        for (String event: inputWidget.getStringAttribute("event")) {
            GeneratorInformationElement newElement = new GeneratorInformationElement("inputfieldmethod", inputWidget.getID() + "_" + event, inputWidget,
                    inputWidget.getAttributes());
            newElement.removeAttribute("event");
            newElement.removeAttribute("parameter");
            if (isJavaFX) {
                newElement.addAttribute("method", "fireEvent(javafx.event.Event)");
                newElement.addAttribute("parameter", "javaFX:" + event);
            } else if (isSwing) {
                //TODO
            }
            predicates.forEach(predicate -> newElement.addAttribute("predicate", predicate.getID()));
            newElement.addAttribute("nfapredicate", inputWidget.getID() + "NFA#widgetNFA[" + JavaFXNFAs.getEdgeID(event) + "]#GUI");
            newElements.add(newElement);
        }
        return new TransformationResult(settings, newElements);
    }

    // X : Input Widget -> Parameter
    //Dit is om parameter generators te maken voor de Fields!!!
    public static TransformationResult generateParametersFromInputWidgets(Settings settings, GeneratorInformationElement inputWidget){
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        String parameterID = inputWidget.getID() + "_Parameter";
        GeneratorInformationElement newElement = new GeneratorInformationElement("parameter",parameterID, inputWidget,
                "class",inputWidget.getStringAttribute("class").get(0),
                "field",inputWidget.getStringAttribute("field").get(0));
        newElements.add(newElement);
        return new TransformationResult(settings, newElements);
    }

    // X : Input Widget -> Predicates
    public static TransformationResult generatePredicatesFromInputWidget(Settings settings, GeneratorInformationElement inputWidget, GeneratorInformationElement fieldParameter) {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        String predicateID = inputWidget.getID() + "_isAccessible";

        Map<String,Object> predicateMapping = PredicateTransformer.getMethodData("javaFX:isAccessible");
        GeneratorInformationElement newElement = new GeneratorInformationElement("predicate",predicateID, inputWidget,
                "class", predicateMapping.get("class") ,
                "method",predicateMapping.get("method"),
                "parameter",fieldParameter.getID());
        newElements.add(newElement);
        return new TransformationResult(settings, newElements);
    }

    private static boolean isJavaFXWidget(GeneratorInformationElement inputWidget){
        return true; //TODO: implement
    }

    private static boolean isSwingWidget(GeneratorInformationElement inputWidget){
        return false; //TODO: implement
    }
}
