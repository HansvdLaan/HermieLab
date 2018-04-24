package hermielab.generator.settings.transformations;

import net.automatalib.commons.util.Pair;
import hermielab.generator.settings.Settings;
import hermielab.generator.settings.containers.GeneratorInformationElement;
import hermielab.generator.settings.transformations.exceptions.IncompleteElementException;
import hermielab.generator.settings.transformations.exceptions.IncorrectElementException;
import hermielab.generator.settings.transformers.PredicateTransformer;

import java.util.*;
import java.util.function.BiFunction;

public class HelperFunctions {

    // X : Any, F -> F(Any)
    public static TransformationResult process(BiFunction<Settings, Settings, TransformationResult> transformation, Settings settings, GeneratorInformationElement element) {
        return null;
    }

    // X : Any -> Reference: {Output Function}
    public static TransformationResult processOutputFunctionReferences(Settings settings, GeneratorInformationElement element) {
        List<String> outputs = element.getStringAttribute("output");
        outputs.forEach(id -> addReference(settings, "output", id));
        return new TransformationResult(settings, new HashSet<>());
    }

    // X : Any -> Parameter Generator Reference: {Parameter Generator}
    public static TransformationResult processParameterGeneratorReferences(Settings settings, GeneratorInformationElement element) {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        List<String> parametergenerators = element.getStringAttribute("parameter");
        for (String parametergenerator : parametergenerators) {
            String parametergeneratorID = parametergenerator.split("\\(")[0];
            boolean isFromLibrary = parametergeneratorID.matches(".*:+.*"); //we assume that if a predicate is written like this it's always from a library
            if (!isFromLibrary) {
                addReference(settings, "parametergenerator", parametergenerator);
            }
        }
        return new TransformationResult(settings, newElements);
    }

    // X : Any -> Predicate Reference: {Predicate}
    //If it's a library function, it will replace the reference with the fieldID of a newly created predicate
    //If it's not a library function, it will just create a reference
    public static TransformationResult processPredicateReferences(Settings settings, GeneratorInformationElement element) {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        List<String> predicates = element.getStringAttribute("predicate");
        for (String predicate : predicates) {
            String predicateID = predicate.split("\\(")[0];
            List<String> parameters = Arrays.asList(predicate.replaceAll("(.*)\\(|\\)", "").split(","));
            boolean isFromLibrary = predicateID.matches(".*:+.*"); //we assume that if a predicate is written like this it's always from a library
            if (isFromLibrary) {
                Map<String, Object> predicateMapping = PredicateTransformer.getMethodData(predicateID);
                GeneratorInformationElement newElem = new GeneratorInformationElement("predicate", predicate, element,
                        "class", predicateMapping.get("class"),
                        "method", predicateMapping.get("method"),
                        "parameter", parameters);
//                element.getOrdenedAttributes().get("predicate").remove(predicate);
//                element.addAttribute("predicate", predicate);
                if (settings.getSettingsByTypeAndID(newElem.getType(), newElem.getID()) == null) {
                    newElements.add(newElem);
                    addElement(settings, newElem);
                } else {
                    element.addChild(settings.getSettingsByTypeAndID(newElem.getType(), newElem.getID()));
                }
            } else {
                addReference(settings, "predicate", predicate);
            }
        }
        return new TransformationResult(settings, newElements);
    }

    // X : Any -> Self
    public static TransformationResult addSelf(Settings settings, GeneratorInformationElement element) {
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        if (element == null) {
            throw new IncompleteElementException("cannot add self when self is null");
        } else {
            if (!settings.containsElement(element)) {
                newElements.add(element);
                addElement(settings, element);
            }
        }
        return new TransformationResult(settings, newElements);
    }

    // X : -> Reference
    public static TransformationResult addReference(Settings settings, String type, String id) {
        if (settings.getSettingsByTypeAndID(type, id) == null) {
            settings.addReference(type, id);
        }
        return new TransformationResult(settings, new HashSet<>());
    }

    // X : Any -> Self
    public static TransformationResult addElement(Settings settings, GeneratorInformationElement element) {
        if (element == null) {
            throw new IncorrectElementException("cannot add an element which is null");
        }
        Set<GeneratorInformationElement> newElements = new HashSet<>();
        settings.addElement(element);
        if (element.hasParents()) {
            Pair<String, String> creatorTypeAndID = element.getParents().get(element.getParents().size() - 1);
            GeneratorInformationElement creator = settings.getSettingsByTypeAndID(creatorTypeAndID.getFirst(), creatorTypeAndID.getSecond());
            if (creator != null) {
                creator.addChild(element);
            }
        }
        return new TransformationResult(settings, newElements);
    }

    public static boolean elementContainsVariables(GeneratorInformationElement element, String... parameters) {
        return true; //TODO
    }

    public static boolean elementContainsNonNullVariables(GeneratorInformationElement element, String... parameters) {
        return true; //TODO
    }

    //TODO, state how here only the most recent version are taken, i.e. init can be overwritten by newly generated hermielab.generator.settings.
    public static Map<String, Map<String, GeneratorInformationElement>> combineAllSettings(Settings s1, Settings s2) {
        Map mergedSettings = new HashMap();
        mergedSettings.putAll(s1.getAllSettings());
        mergedSettings.putAll(s2.getAllSettings());
        return mergedSettings;
    }

    public static Map<String, GeneratorInformationElement> combineTypeSettings(String type, Settings s1, Settings s2) {
        Map mergedSettings = new HashMap();
        mergedSettings.putAll(s1.getSettingsByType(type));
        mergedSettings.putAll(s2.getSettingsByType(type));
        return mergedSettings;
    }

    public static Stack<String> getParameterStack(GeneratorInformationElement element) {
        Stack<String> parameters = new Stack<String>();
        List<String> parameterList = element.getStringAttribute("parameter");
        Collections.reverse(parameterList);
        parameters.addAll(parameterList);
        return parameters;
    }

    public static int determineAmountOfParameters(String methodSignature) {
        String[] splittedMethod = methodSignature.split("[()]");
        if (splittedMethod.length == 2) {
            String parameterString = methodSignature.split("[()]")[1];
            return parameterString.split(",").length;
        } else {
            return 0;
        }
    }

    public static GeneratorInformationElement getParent(Settings settings, GeneratorInformationElement element) {
        int lastIndex = element.getParents().size() - 1;
        Pair<String, String> parent = element.getParents().get(lastIndex);
        return settings.getSettingsByTypeAndID(parent.getFirst(), parent.getSecond());
    }

    public static Set<GeneratorInformationElement> getDescendants(Settings settings, GeneratorInformationElement element) {
        Set<GeneratorInformationElement> children = new HashSet<>();
        element.getChildren().forEach(pair -> children.add(settings.getSettingsByTypeAndID(pair.getFirst(), pair.getSecond())));
        Set<GeneratorInformationElement> descendants = new HashSet<>();
        for (GeneratorInformationElement child: children) {
            descendants.addAll(getDescendants(settings,child));
        }
        descendants.addAll(children);
        return descendants;
    }

}
