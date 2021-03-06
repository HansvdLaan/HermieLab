package hermielab.processor;

import hermielab.annotations.abstraction.FieldMethodSymbol;
import hermielab.annotations.abstraction.FunctionSymbol;
import hermielab.annotations.abstraction.WidgetSymbol;
import hermielab.annotations.abstraction.repeat.FieldMethodSymbols;
import hermielab.annotations.abstraction.repeat.FunctionSymbols;
import hermielab.annotations.abstraction.repeat.WidgetSymbols;
import hermielab.annotations.concrete.InputFieldMethod;
import hermielab.annotations.concrete.InputFunction;
import hermielab.annotations.concrete.InputWidget;
import hermielab.annotations.concrete.OutputFieldMethod;
import hermielab.annotations.concrete.OutputFunction;
import hermielab.annotations.processor.AnnotatedElement;
import hermielab.annotations.setup.Initialize;
import hermielab.annotations.setup.PostInputInvocation;
import hermielab.annotations.setup.PostOutputInvocation;
import hermielab.annotations.setup.PostQuery;
import hermielab.annotations.setup.PreInputInvocation;
import hermielab.annotations.setup.PreOutputInvocation;
import hermielab.annotations.setup.PreQuery;
import hermielab.annotations.setup.Shutdown;
import hermielab.annotations.setup.Start;
import hermielab.annotations.util.Parameter;
import hermielab.annotations.util.ParameterGenerator;
import hermielab.annotations.util.Predicate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.XMLWriter;

@SupportedAnnotationTypes("hermielab.annotations.setup.Start")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AnnotationsProcessor extends AbstractProcessor {

    private static List<String> generalSettings = Arrays.asList("ID","order","experiment","automaton","empty","ignore",
            "input","event(#[0-9]*)?","output(#[0-9]*)?","transform","param#[0-9]+","predicate(#[0-9]*)?","predicate#nfa(#[0-9]*)?","predicate#nfa#hermielab.generator.settings(#[0-9]*)?","class","field","method(#[0-9]*)?");
    private boolean initialProcessingDone = false;

  @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        initialProcessingDone = false;
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Annotation Processor instantiated");
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Annotation Processor processsing started");

        if (initialProcessingDone){
            return initialProcessingDone;
        }

        // root elements
        Document document = DocumentHelper.createDocument();
        document.setName("hermielabsettings");
        org.dom4j.Element root = document.addElement( "settings" );

        List<AnnotatedElement> initializeFunctions = getOrderedElements(Initialize.class, roundEnv);
        List<AnnotatedElement> startFunctions = getOrderedElements(Start.class, roundEnv);
        List<AnnotatedElement> shutdownFunctions = getOrderedElements(Shutdown.class, roundEnv);

        List<AnnotatedElement> inputWidgets = getUnorderedElements(InputWidget.class, roundEnv);
        List<AnnotatedElement> inputFieldMethods = getUnorderedElements(InputFieldMethod.class, roundEnv);
        List<AnnotatedElement> outputFieldMethods = getUnorderedElements(OutputFieldMethod.class, roundEnv);

        List<AnnotatedElement> functionSymbols = getUnorderedElements(FunctionSymbol.class, roundEnv);
        functionSymbols.addAll(getUnorderedElements(FunctionSymbols.class, roundEnv));

        List<AnnotatedElement> widgetSymbols = getUnorderedElements(WidgetSymbol.class, roundEnv);
        widgetSymbols.addAll(getUnorderedElements(WidgetSymbols.class, roundEnv));

        List<AnnotatedElement> fieldMethodSymbols = getUnorderedElements(FieldMethodSymbol.class, roundEnv);
        fieldMethodSymbols.addAll(getUnorderedElements(FieldMethodSymbol.class, roundEnv));


        List<AnnotatedElement> inputFunctions = getUnorderedElements(InputFunction.class, roundEnv);
        List<AnnotatedElement> outputFunctions = getUnorderedElements(OutputFunction.class, roundEnv);
        List<AnnotatedElement> predicateFunctions = getUnorderedElements(Predicate.class, roundEnv);
        List<AnnotatedElement> parameters = getUnorderedElements(Parameter.class, roundEnv);
        List<AnnotatedElement> parameterGeneratorFunctions = getUnorderedElements(
            ParameterGenerator.class, roundEnv);

        List<AnnotatedElement> preQueryInvocationFunctions = getOrderedElements(PreQuery.class, roundEnv);
        List<AnnotatedElement> postQueryInvocationFunctions = getOrderedElements(PostQuery.class, roundEnv);
        List<AnnotatedElement> preInputInvocationFunctions = getOrderedElements(PreInputInvocation.class, roundEnv);
        List<AnnotatedElement> postInputInvocationFunctions = getOrderedElements(
            PostInputInvocation.class, roundEnv);
        List<AnnotatedElement> preOutputInvocationFunctions = getOrderedElements(
            PreOutputInvocation.class, roundEnv);
        List<AnnotatedElement> postOutputInvocationFunctions = getOrderedElements(
            PostOutputInvocation.class, roundEnv);

        System.out.println("inputsymbols:" + functionSymbols);
        writeElementsToXML(root, widgetSymbols, WidgetSymbol.class);
        writeElementsToXML(root, functionSymbols, FunctionSymbol.class);
        writeElementsToXML(root, fieldMethodSymbols, FieldMethodSymbol.class);

        writeElementsToXML(root, inputWidgets, InputWidget.class);
        writeElementsToXML(root, inputFieldMethods, InputFieldMethod.class);
        writeElementsToXML(root, outputFieldMethods, OutputFieldMethod.class);

        writeElementsToXML(root, inputFunctions, InputFunction.class);

        System.out.println("output functions:" + outputFunctions);
        writeElementsToXML(root, outputFunctions, OutputFunction.class);
        writeElementsToXML(root, predicateFunctions, Predicate.class);
        writeElementsToXML(root, parameters, Parameter.class);
        writeElementsToXML(root, parameterGeneratorFunctions, ParameterGenerator.class);

        writeElementsToXML(root, initializeFunctions, Initialize.class);
        writeElementsToXML(root, startFunctions, Start.class);
        writeElementsToXML(root, shutdownFunctions, Shutdown.class);

        writeElementsToXML(root, preQueryInvocationFunctions, PreQuery.class);
        writeElementsToXML(root, postQueryInvocationFunctions, PostQuery.class);
        writeElementsToXML(root, preInputInvocationFunctions, PreInputInvocation.class);
        writeElementsToXML(root, postInputInvocationFunctions, PostInputInvocation.class);
        writeElementsToXML(root, preOutputInvocationFunctions, PreOutputInvocation.class);
        writeElementsToXML(root, postOutputInvocationFunctions, PostOutputInvocation.class);


        // Pretty print the document to a file
        File file = new File("hermielabsettings.xml");
        org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
        XMLWriter writer;
        try {
            FileOutputStream fop = new FileOutputStream(file);
            writer = new XMLWriter( fop, format );
            writer.write( document );
        } catch (IOException e) {
            e.printStackTrace();
        }

        initialProcessingDone = true;
        return initialProcessingDone;
    }

    public List<AnnotatedElement> getUnorderedElements(Class annotationType, RoundEnvironment environment) {
        List<AnnotatedElement> annotatedElements = new LinkedList<>();
        for (Object obj : environment.getElementsAnnotatedWith(annotationType)) {
            Element element = (Element) obj;
            int order;
            String id;
            if (annotationType.equals(Initialize.class)) {
                Initialize annotation = element.getAnnotation(Initialize.class);
                Map<String,String> parameters = new HashMap<>();
                order = annotation.order();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element,parameters, order));
            }
            else if (annotationType.equals(InputFunction.class)){
                InputFunction annotation = element.getAnnotation(InputFunction.class);
                Map<String,String> parameters = new HashMap<>();
                id = annotation.ID();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                parameters.putAll(parseParameters("parameter", annotation.parameters()));
                parameters.putAll(parseParameters("predicate", annotation.predicates()));
                parameters.putAll(parseParameters("nfapredicate", annotation.nfapredicates()));
                annotatedElements.add(new AnnotatedElement(element, parameters, id));
            }
            else if (annotationType.equals(InputFieldMethod.class)){
                InputFieldMethod annotation = element.getAnnotation(InputFieldMethod.class);
                Map<String,String> parameters = new HashMap<>();
                id = annotation.fieldID();

                parameters.putAll(parseParameters("method", annotation.fieldMethods()));
                parameters.putAll(parseParameters("parameter", annotation.parameters()));
                parameters.putAll(parseParameters("predicate", annotation.predicates()));
                parameters.putAll(parseParameters("nfapredicate", annotation.nfapredicates()));
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("field", element.toString());
                annotatedElements.add(new AnnotatedElement(element, parameters, id));
            }
            else if (annotationType.equals(FieldMethodSymbols.class)){
                for (FieldMethodSymbol annotation: element.getAnnotation(FieldMethodSymbols.class).value()){
                    annotatedElements.add(generateFieldMethodSymbolElement(annotation, element));
                }
            }
            else if (annotationType.equals(FieldMethodSymbol.class)) {
                FieldMethodSymbol annotation = element.getAnnotation(FieldMethodSymbol.class);
                annotatedElements.add(generateFieldMethodSymbolElement(annotation, element));
            }
            else if (annotationType.equals(FunctionSymbols.class)){
                for (FunctionSymbol annotation: element.getAnnotation(FunctionSymbols.class).value()){
                    annotatedElements.add(generateFunctionSymbolElement(annotation, element));
                }
            }
            else if (annotationType.equals(FunctionSymbol.class)) {
                FunctionSymbol annotation = element.getAnnotation(FunctionSymbol.class);
                annotatedElements.add(generateFunctionSymbolElement(annotation, element));
            }
            else if (annotationType.equals(InputWidget.class)) {
                InputWidget annotation = element.getAnnotation(InputWidget.class);
                Map<String,String> parameters = new HashMap<>();
                id = annotation.widgetID();

                parameters.putAll(parseParameters("event", annotation.events()));
                parameters.putAll(parseParameters("parameter", annotation.parameters()));
                parameters.putAll(parseParameters("predicate", annotation.predicates()));
                parameters.putAll(parseParameters("nfapredicate", annotation.nfapredicates()));
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("field", element.toString());
                annotatedElements.add(new AnnotatedElement(element,parameters,id));
            }
            else if (annotationType.equals(WidgetSymbols.class)){
                for (WidgetSymbol annotation: element.getAnnotation(WidgetSymbols.class).value()){
                    annotatedElements.add(generateWidgetSymbolElement(annotation, element));
                }
            }
            else if (annotationType.equals(WidgetSymbol.class)) {
                WidgetSymbol annotation = element.getAnnotation(WidgetSymbol.class);
                annotatedElements.add(generateWidgetSymbolElement(annotation, element));
            }
            else if (annotationType.equals(OutputFunction.class)){
                OutputFunction annotation = element.getAnnotation(OutputFunction.class);
                Map<String,String> parameters = new HashMap<>();
                id = annotation.ID();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element,parameters, id));
            }
            else if (annotationType.equals(OutputFieldMethod.class)){
                OutputFieldMethod annotation = element.getAnnotation(OutputFieldMethod.class);
                id = annotation.ID();
                Map<String,String> parameters = new HashMap<>();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("field", element.toString());
                parameters.put("method", annotation.fieldMethod());
                annotatedElements.add(new AnnotatedElement(element,parameters, id));
            }
            else if (annotationType.equals(Parameter.class)) {
                Parameter annotation = element.getAnnotation(Parameter.class);
                Map<String,String> parameters = new HashMap<>();
                id = annotation.ID();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("field", element.toString());
                annotatedElements.add(new AnnotatedElement(element, parameters, id));
            }
            else if (annotationType.equals(ParameterGenerator.class)) {
                ParameterGenerator annotation = element.getAnnotation(ParameterGenerator.class);
                Map<String,String> parameters = new HashMap<>();
                id = annotation.ID();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element, parameters, id));
            }
            else if (annotationType.equals(PostInputInvocation.class)) {
                PostInputInvocation annotation = element.getAnnotation(PostInputInvocation.class);
                Map<String,String> parameters = new HashMap<>();
                order = annotation.order();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element, parameters, order));
            }
            else if (annotationType.equals(PostOutputInvocation.class)) {
                PostOutputInvocation annotation = element.getAnnotation(PostOutputInvocation.class);
                Map<String,String> parameters = new HashMap<>();
                order = annotation.order();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element, parameters, order));
            }
            else if (annotationType.equals(PostQuery.class)) {
                PostQuery annotation = element.getAnnotation(PostQuery.class);
                Map<String,String> parameters = new HashMap<>();
                order = annotation.order();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element, parameters, order));
            }
            else if (annotationType.equals(Predicate.class)) {
                Predicate annotation = element.getAnnotation(Predicate.class);
                Map<String,String> parameters = new HashMap<>();
                id = annotation.ID();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element,parameters, id));
            }
            else if (annotationType.equals(PreInputInvocation.class)) {
                PreInputInvocation annotation = element.getAnnotation(PreInputInvocation.class);
                Map<String,String> parameters = new HashMap<>();
                order = annotation.order();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element, parameters, order));
            }
            else if (annotationType.equals(PreOutputInvocation.class)) {
                PreOutputInvocation annotation = element.getAnnotation(PreOutputInvocation.class);
                Map<String,String> parameters = new HashMap<>();
                order = annotation.order();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element,parameters, order));
            }
            else if (annotationType.equals(PreQuery.class)) {
                PreQuery annotation = element.getAnnotation(PreQuery.class);
                Map<String,String> parameters = new HashMap<>();
                order = annotation.order();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element,parameters, order));
            }
            else if (annotationType.equals(Shutdown.class)) {
                Shutdown annotation = element.getAnnotation(Shutdown.class);
                Map<String,String> parameters = new HashMap<>();
                order = annotation.order();
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element, parameters, order));
            } else if (annotationType.equals(Start.class)) {
                Start annotation = element.getAnnotation(Start.class);
                Map<String,String> parameters = new HashMap<>();
                parameters.put("automaton",annotation.automaton());
                parameters.put("class", element.getEnclosingElement().toString());
                parameters.put("method", element.toString());
                annotatedElements.add(new AnnotatedElement(element, parameters, 0));
            }
        }

        return annotatedElements;

    }


    public List<AnnotatedElement> getOrderedElements(Class annotationType, RoundEnvironment environment) {
        List<AnnotatedElement> elements = getUnorderedElements(annotationType, environment);
        Collections.sort(elements);
        return elements;
    }

    private void writeElementsToXML(org.dom4j.Element typeElem, List<AnnotatedElement> elements, Class annotationClass) {
        for (AnnotatedElement element : elements) {

            org.dom4j.Element xmlElem = typeElem.addElement(annotationClass.getSimpleName().toLowerCase());
            if (element.getId() != null) {
                xmlElem.addElement("ID").addText(element.getId());
            }
            if (element.getOrder() > -2) {
                xmlElem.addElement("order").addText(String.valueOf(element.getOrder()));
            }
            for (String variable : generalSettings) {
                List<String> matchingParameters = element.getParameters().keySet().stream().filter(key -> key.matches(variable)).collect(
                    Collectors.toList());
                //System.out.println("Matching parameters:" + matchingParameters);
                for (String param : matchingParameters) {
                    xmlElem.addElement(param)
                            .addText(element.getParameters().get(param));
                }
            }
            List<String> otherParameters = element.getParameters().keySet().stream().filter(key ->
                    generalSettings.stream().noneMatch(var -> key.matches(var))).collect(Collectors.toList());
            System.out.println("otherparams:" + otherParameters);
            for (String param : otherParameters) {
                xmlElem.addElement(param)
                        .addText(element.getParameters().get(param));
            }
        }
    }

    //TODO: test what happens if the parameters are "empty, i.e. without any string"
    private Map<String,String> parseParameters(String[] parametersStrings) {
        Map<String,String> parameters = new HashMap<>();
        for (String params: parametersStrings) {
            if (!params.trim().equals("")) {
                String[] variables = params.split("[:]");
                if (variables.length == 1) {
                    parameters.put(params, "True");
                } else if (variables.length >= 2) {
                    parameters.put(variables[0].replace("#",""), params.substring(variables[0].length() +1 , params.length() ));
                }
            }
        }
        return parameters;
    }

    private Map<String,String> parseParameters(String parameterName, String[] values){
        Map<String,String> parameters = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            parameters.put(parameterName + "-" + i,values[i]);
        }
        return parameters;
    }

    private AnnotatedElement generateFunctionSymbolElement(FunctionSymbol annotation, Element element){
        String id = annotation.symbolID();
        Map<String,String> parameters = new HashMap<>();
        parameters.putAll(parseParameters("parameter", annotation.parameters()));
        parameters.putAll(parseParameters("predicates", annotation.predicates()));
        parameters.putAll(parseParameters("nfapredicates", annotation.nfapredicates()));
        parameters.put("input",id+"InputFunction");
        parameters.put("output", annotation.outputID());
        parameters.put("class", element.getEnclosingElement().toString());
        parameters.put("method", element.toString());
        return new AnnotatedElement(element,parameters,id);
    }

    private AnnotatedElement generateWidgetSymbolElement(WidgetSymbol annotation, Element element) {
        String id = annotation.widgetID();
        Map<String,String> parameters = new HashMap<>();
        parameters.put("class", element.getEnclosingElement().toString());
        parameters.put("field", element.toString());
        parameters.putAll(parseParameters("event", annotation.events()));
        parameters.putAll(parseParameters("output", annotation.outputIDs()));
        parameters.putAll(parseParameters("predicates", annotation.predicates()));
        parameters.putAll(parseParameters("nfapredicates", annotation.nfapredicates()));
        return new AnnotatedElement(element,parameters,id);
    }


    private AnnotatedElement generateFieldMethodSymbolElement(FieldMethodSymbol annotation, Element element){
        String id = annotation.fieldID();
        Map<String,String> parameters = new HashMap<>();
        parameters.putAll(parseParameters("method", annotation.fieldMethods()));
        parameters.putAll(parseParameters("output", annotation.outputIDs()));
        parameters.putAll(parseParameters("parameter", annotation.parameters()));
        parameters.putAll(parseParameters("predicates", annotation.predicates()));
        parameters.putAll(parseParameters("nfapredicates", annotation.nfapredicates()));
        parameters.put("class", element.getEnclosingElement().toString());
        parameters.put("field", element.toString());
        return new AnnotatedElement(element, parameters, id);
    }
}

