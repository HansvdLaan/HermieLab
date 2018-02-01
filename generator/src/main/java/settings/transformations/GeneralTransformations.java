package settings.transformations;

import package2.Settings;
import settings.SettingsPreProcessorUtils;
import settings.containers.GeneratorInformationElement;
import settings.transformers.PredicateTransformer;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by Hans on 19-1-2018.
 */
public class GeneralTransformations {


    public static BiFunction<Settings,Settings,Settings> FunctionSymbolTransformation = (init, settings) -> {
        Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType("functionsymbol");
        for (String id: elementMap.keySet()){

            Map<String, Object> inputFunctionData = new HashMap<>();
            GeneratorInformationElement element = elementMap.get(id);
            inputFunctionData.putAll(element.getData());
            filterByRegex(inputFunctionData, Arrays.asList("input","output"));
            String inputID = id + "InputFunction";
            GeneratorInformationElement inputFunction = new GeneratorInformationElement("inputfunction",inputID,inputFunctionData);
            addElementToSettings(settings, inputFunction);

            handleOutputReferences(settings, element);
            handlePredicateReferences(settings, element);
            handleParameterReferences(settings, element);
        }
        return settings;
    };

    public static BiFunction<Settings,Settings,Settings> WidgetSymbolTransformation = (init, settings) -> {
        Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType("widgetsymbol");
        for (String id: elementMap.keySet()){

            Map<String, Object> inputFunctionData = new HashMap<>();
            GeneratorInformationElement element = elementMap.get(id);
            inputFunctionData.putAll(element.getData());
            filterByRegex(inputFunctionData, Arrays.asList("output(#[0-9]*)?"));

            List<String> orderedEvents = getOrderedParameters(element, "event(#(.*))?");
            List<String> orderedInputIDS = new LinkedList<>();
            //TODO: event map structure to allow for multiple of the same events!
            for (int i = 0; i < orderedEvents.size(); i++) {
                String event = orderedEvents.get(i);
                String inputID = id + event.substring(0,1).toUpperCase() + event.substring(1);
                orderedInputIDS.add(inputID);
                inputFunctionData.put("input#" + i, inputID);
            }
            List<String> orderedOutputIDs = getOrderedParameters(element, "output(#(.*))?");
            addElementsToSettings(settings, generateSymbols(id, orderedInputIDS, orderedOutputIDs));

            GeneratorInformationElement inputWidget = new GeneratorInformationElement("inputwidget",id + "InputWidget",inputFunctionData);

            handleOutputReferences(settings, element);
            handlePredicateReferences(settings, element);
            handleParameterReferences(settings, element);


        }
        return settings;
    };

    public static BiFunction<Settings,Settings,Settings> FieldMethodSymbolTransformation = (init, settings) -> {
        Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType("fieldmethodsymbol");
        for (String id: elementMap.keySet()){

            Map<String, Object> inputFunctionData = new HashMap<>();
            GeneratorInformationElement element = elementMap.get(id);
            inputFunctionData.putAll(element.getData());
            filterByRegex(inputFunctionData, Arrays.asList("output(#[0-9]*)?"));

            List<String> orderedMethods = getOrderedParameters(element, "method(#(.*))?");
            List<String> orderedInputIDS = new LinkedList<>();
            Map<String, Integer> idCounterMap = new HashMap<>();
            for (int i = 0; i < orderedMethods.size(); i++) {
                String event = orderedMethods.get(i);
                String inputID = id + event.substring(0,1).toUpperCase() + event.substring(1);
                if (idCounterMap.containsKey(inputID)){
                    idCounterMap.put(inputID, idCounterMap.get(inputID) + 1);
                    inputID += idCounterMap.get(inputID);
                } else {
                    idCounterMap.put(inputID, 1);
                }
                orderedInputIDS.add(inputID);
                inputFunctionData.put("input#" + i, inputID);
            }

            List<String> orderedOutputIDs = getOrderedParameters(element, "output(#(.*))?");
            addElementsToSettings(settings, generateSymbols(id, orderedInputIDS, orderedOutputIDs));

            GeneratorInformationElement inputWidget = new GeneratorInformationElement("inputfieldmethod",id + "FieldMethod",inputFunctionData);
            addElementToSettings(settings, inputWidget);

            handleOutputReferences(settings, element);
            handlePredicateReferences(settings, element);
            handleParameterReferences(settings, element);

        }
        return settings;
    };

    public static BiFunction<Settings,Settings,Settings> InputWidgetTransformation = (init, settings) -> {
        Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType("inputwidget");
        for (String id: elementMap.keySet()) {

            GeneratorInformationElement element = elementMap.get(id);
            String clazz = element.getData().get("class").toString();
            String field = element.getData().get("field").toString();
            List<String> events = getOrderedParameters(element,"event(#[0-9]*)?");

            Map<String, Integer> eventCounterMap = new HashMap<>();

            Map<String, Object> inputFieldMethodData = new HashMap<>();
            inputFieldMethodData.putAll(element.getData());
            filterByRegex(inputFieldMethodData, Arrays.asList("event(#[0-9]*)?"));

            //TODO: take into account that users could also add parameters!!
            inputFieldMethodData.put("predicate#40", "javaFX:isAccessible(field)");

            for (int i = 0; i < events.size(); i++) {
                String event = events.get(i);
                eventCounterMap.putIfAbsent(event, 0);
                eventCounterMap.put(event, eventCounterMap.get(event) + 1);
                inputFieldMethodData.put("nfapredicate#40", generateNFAPredicate(id, event));
                inputFieldMethodData.put("param#0", "Event:" + event);

                try {
                    VariableElement fieldElement = SettingsPreProcessorUtils.getField(SettingsPreProcessorUtils.getClass(clazz), field);
                    TypeMirror fieldType = fieldElement.asType();
                    TypeMirror javaFXType = SettingsPreProcessorUtils.getClass("javafx.scene.Node").asType();
                    if (SettingsPreProcessorUtils.getInstance().getPrEnv().getTypeUtils().isAssignable(fieldType, javaFXType)) {
                        inputFieldMethodData.put("method#" + i, "fireEvent(javafx.event.Event)");
                    } else if (false) {
                        //TODO: handle javaFX case
                    } else {
                        throw new IllegalStateException("Invalid Widget:" + id + ", only JavaFX and Swing widgets are supported.");
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

                GeneratorInformationElement concreteInputFieldMethod = new GeneratorInformationElement("inputfieldmethod",id, inputFieldMethodData);
                addElementToSettings(settings, concreteInputFieldMethod);

            handlePredicateReferences(settings, element);
            handleParameterReferences(settings, element);
        }

        return settings;
    };

    public static BiFunction<Settings,Settings,Settings> InputFieldMethodTransformation = (init, settings) -> {
        Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType("inputfieldmethod");
        Map<String, Integer> methodCounterMap = new HashMap<>();
        for (String id: elementMap.keySet()) {

            GeneratorInformationElement element = elementMap.get(id);

            List<String> methods = getOrderedParameters(element, "method(#[0-9]*)?");
            List<String> inputIDs = getOrderedParameters(element, "input(#[0-9]*)?");
            List<String> parameters = getOrderedParameters(element,"param(#[0-9]*)?");
            int paramCounter = 0;


            for (int i = 0; i < methods.size(); i++) {
                String method = methods.get(i);
                String methodName = method.split("[(]")[0];
                methodCounterMap.putIfAbsent(methodName, 0);
                methodCounterMap.put(methodName, methodCounterMap.get(methodName) + 1);

                Map<String, Object> inputFunctionData = new HashMap<>();
                inputFunctionData.putAll(element.getData());

                String inputID = inputIDs.size() > i? inputIDs.get(i) : id + methodName + methodCounterMap.get(methodName);

                filterByRegex(inputFunctionData, Arrays.asList("method(#[0-9]*)?","param(#[0-9]*)?","input(#[0-9]*)?"));
                for (int v = 1; v <= determineAmountOfVariables(method); v++) {
                    inputFunctionData.put("param#" + v, parameters.get(paramCounter));
                    paramCounter = (paramCounter + 1) % parameters.size();
                }


                GeneratorInformationElement concreteInputFunction = new GeneratorInformationElement("inputfunction", inputID, inputFunctionData);
                addElementToSettings(settings, concreteInputFunction);

                handlePredicateReferences(settings, element);
                handleParameterReferences(settings, element);
            }
        }

        return settings;
    };

    public static BiFunction<Settings,Settings,Settings> copyInitToSettings = (init,settings) -> {
        for (String type: init.getAllSettings().keySet()){
            settings.addSettingsByType(type, new HashMap<>());
            for (String id: init.getSettingsByType(type).keySet()){
                GeneratorInformationElement element = init.getSettingsByTypeAndID(type, id);
                Map<String,Object> data = new HashMap<String,Object>(element.getData());
                GeneratorInformationElement elementCopy = new GeneratorInformationElement(element.getType(), element.getId(), data);
                settings.addSettingsByTypeAndID(type, id, elementCopy);
            }
        }
        return settings;
    };

    public static BiFunction<Settings,Settings,Settings> stringToReflectionTransformation = (init,settings) -> {
        for (String type : settings.getAllSettings().keySet()) {
            for (String id : settings.getSettingsByType(type).keySet()) {
                GeneratorInformationElement element = settings.getSettingsByTypeAndID(type, id);
                if (element.getData().containsKey("class")) {
                    String clazz = element.getData().get("class").toString();
                    try {
                        element.getData().put("class", SettingsPreProcessorUtils.getClass(clazz));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (element.getData().containsKey("field")) {
                    if (element.getData().get("class") instanceof TypeElement) {
                        TypeElement clazz = (TypeElement) element.getData().get("class");
                        String field = element.getData().get("field").toString();
                        try {
                            element.getData().put("field", SettingsPreProcessorUtils.getField(clazz, field));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
                if (element.getData().containsKey("method")) {
                    if (element.getData().get("class") instanceof TypeElement) {
                        TypeElement clazz = (TypeElement) element.getData().get("class");
                        String method = element.getData().get("method").toString();
                        if (element.getData().containsKey("field") && element.getData().get("field") instanceof VariableElement) {
                            VariableElement field = (VariableElement) element.getData().get("field");
                            try {
                                element.getData().put("method", SettingsPreProcessorUtils.getMethod(clazz, field, method));
                            } catch (NoSuchMethodException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                element.getData().put("method", SettingsPreProcessorUtils.getMethod(clazz, method));
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return settings;
    };

    public static BiFunction<Settings,Settings,Settings> ParameterToParameterGeneratorTransformation = (init, settings) -> {
        Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType("parameter");
        for (String id: elementMap.keySet()){

            Map<String, Object> parameterGeneratorData = new HashMap<>();
            GeneratorInformationElement element = elementMap.get(id);
            parameterGeneratorData.putAll(element.getData());
            String paramGeneratorID = id + "ParameterGenerator";

            GeneratorInformationElement parameterGenerator = new GeneratorInformationElement("parametergenerator",paramGeneratorID,parameterGeneratorData);

            addElementToSettings(settings, parameterGenerator);
        }
        return settings;
    };

    private static List<String> getOrderedParameters(GeneratorInformationElement element, String regex){
        List<String> orderedParameterIDs = new ArrayList<>(element.getDataSubset(regex).keySet());
        Collections.sort(orderedParameterIDs);
        return orderedParameterIDs.stream().map(key -> element.getData().get(key).toString()).collect(Collectors.toList());
    }

    private static int determineAmountOfVariables(String methodSignature){
        String parameterString = methodSignature.split("[()]")[1];
        return parameterString.split(",").length;
    }

    private static Map<String, GeneratorInformationElement> getElements(Settings init, Settings settings, String type) {
        Map<String, GeneratorInformationElement> elementMap = init.getSettingsByType(type);
        elementMap.putAll(settings.getSettingsByType(type));
        return elementMap;
    }

    private static void addElementsToSettings(Settings settings, Collection<GeneratorInformationElement> elements){
        for (GeneratorInformationElement element: elements){
            addElementToSettings(settings, element);
        }
    }

    private static void addElementToSettings(Settings settings, GeneratorInformationElement element){
        if(settings.getSettingsByTypeAndID(element.getType(), element.getId()) == null) {
            settings.addSettingsByTypeAndID(element.getType(), element.getId(), element);
        } else {
            throw new IllegalStateException("An concrete input function of type: " + element.getType()  +
                    " with ID: " + element.getId() + " already exists in the settings. Elements " +
                    "cannot share the same type and id.");
        }
    }

    public static Set<GeneratorInformationElement> generateSymbols(String parentID, List<String> inputIDs, List<String> outputIDs){
        Set symbols = new HashSet();
        for (int i = 0; i < inputIDs.size(); i++) {
            Map<String, Object> symbolData = new HashMap<>();
            symbolData.put("input",inputIDs.get(i));
            if (outputIDs.size() < i) {
                symbolData.put("output", outputIDs.get(i));
            } else {
                symbolData.put("output",outputIDs.get(outputIDs.size() - 1));
            }
            symbols.add(new GeneratorInformationElement("abstractsymbol",parentID + "Symbol" + i,symbolData));
        }
        return symbols;
    }

    private static void addReferenceToSettings(Settings settings, String type, String id){
        if(settings.getSettingsByTypeAndID(type, id) == null) {
            settings.addSettingsByTypeAndID(type, id, null);
        }
    }

    private static void addReferencesToSettings(Settings settings, String type, List<Object> ids){
        for (Object id: ids){
            addReferenceToSettings(settings, type, id.toString());
        }
    }

    private static String generateNFAPredicate(String field, String event){
        int edgeID = -1;
        switch (event) {
            case "press":
                edgeID = 0;
                break;
            case "release":
                edgeID = 1;
                break;
            case "drag":
                edgeID = 2;
                break;
            default:
                edgeID = -1;
                break;
        }
        return field + "NFA#widgetNFA[" + edgeID + "]#GROUPGUI";
    }

    private static Map<String, Object> filterByRegex(Map<String,Object> data, List<String> regex){
        Set<String> keys = new HashSet<>(data.keySet());
        for (String key: keys){
            if (regex.stream().anyMatch(r -> key.matches(r))){
                data.remove(key);
            }
        }
        return data;
    }

    private static void handleOutputReferences(Settings settings, GeneratorInformationElement element) {
        addReferencesToSettings(settings, "output", new ArrayList<Object>(element.getDataSubset("output(#[0-9]*)?").values()));
    }

    private static void handlePredicateReferences(Settings settings, GeneratorInformationElement element) {
        Map<String,Object> predicates = element.getDataSubset("predicate(#[0-9]*)?");
        int generalPredicateCounter = 1;
        for (String key: predicates.keySet()){
            String predicate = predicates.get(key).toString().split("\\(")[0];
            String[] variables = predicates.get(key).toString().replaceAll("(.*)\\(|\\)","").split(",");
            if (predicate.matches(".*:+.*")){
                String newID = element.getId() + key + generalPredicateCounter;
                element.getData().put(key, newID);
                Map<String,Object> parameters = PredicateTransformer.find(predicate);
                for (int i = 0; i < variables.length; i++){
                    String variable = variables[i];
                    if (!variable.matches(".*:+.*")) {
                        addReferenceToSettings(settings, "parameter", variable);
                    }
                    parameters.put("param#" + i, variables[i]);
                }
                GeneratorInformationElement predicateElem = new GeneratorInformationElement("predicate",newID, parameters);
                if (settings.getSettingsByTypeAndID("predicate",newID) == null) {
                    addElementToSettings(settings, predicateElem); //TODO: clean this
                }
            } else {
                //TODO: Now you can only use variables with pre-gen predicates
                addReferenceToSettings(settings,"predicate",predicate);
            }
        }
    }

    private static void handleParameterReferences(Settings settings, GeneratorInformationElement element){
        addReferencesToSettings(settings, "parametergenerator", new ArrayList<Object>(element.getDataSubset("param(#[0-9]*)?").values().stream()
                .filter(v -> !v.toString().matches(".*:+.*")).collect(Collectors.toList())));
    }


}
