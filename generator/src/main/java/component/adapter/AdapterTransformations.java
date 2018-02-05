package component.adapter;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import component.GeneratedSpecContainer;
import component.utils.GeneratorUtils;
import settings.Settings;
import settings.containers.GeneratorInformationElement;

import java.util.*;
import java.util.function.BiFunction;

public class AdapterTransformations {

    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateConstructor = (generatedSpecs, settings) -> {
        MethodSpec method = MethodSpec.constructorBuilder().build();
        generatedSpecs.addMethodsByTypeAndID("constructor", "constructor", method);
        return generatedSpecs;
    };

    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateWrappedMethods = (generatedSpecs, settings) -> {
        List<String> types = Arrays.asList("parametergenerator", "predicate", "inputfunction", "outputfunction", "start", "initialize", "shutdown"); //TODO: add rest of functions.
        for (String type : types) {
            Map<String, GeneratorInformationElement> elementMap = settings.getSettingsByType(type);
            try {
                boolean isVoid = !(type.equals("parametergenerator") || type.equals("predicate") || type.equals("outputfunction"));
                generatedSpecs.addMethodsByType(type, GeneratorUtils.generateWrappedMethods(elementMap, isVoid));
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return generatedSpecs;
    };

    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateSwitchCaseAggregators = (generatedSpecs, settings) -> {
        List<String> types = Arrays.asList("predicate", "inputfunction", "outputfunction");
        List<String> methodNames = Arrays.asList("checkPredicate", "processInput", "processOutput");
        List<TypeName> returnTypes = Arrays.asList(ParameterizedTypeName.get(Boolean.class),
                ParameterizedTypeName.get(Optional.class,Object.class),
                ParameterizedTypeName.get(Optional.class,Object.class));
        List<List<CodeBlock>> exceptionalCasesList = Arrays.asList(new ArrayList(), new ArrayList<>(), Arrays.asList(generateThisExceptionalCase()));
        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i);
            String methodName = methodNames.get(i);
            TypeName returnType = returnTypes.get(i);
            List<CodeBlock> exceptionalCases = exceptionalCasesList.get(i);
            MethodSpec method = GeneratorUtils.generateSwitchCaseAggregatorMethods(methodName, generatedSpecs.getMethodsByType(type), exceptionalCases, false, returnType);
            generatedSpecs.addMethodsByTypeAndID(type, method.name,method);
        }
        return generatedSpecs;
    };

    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateAggregators = (generatedSpecs, settings) -> {
        List<String> types = Arrays.asList("start", "initialize", "shutdown","prequery","postquery","preinputinvocation","postinputinvocation",
                "preoutputinvocation","postinputinvocation");
        List<String> methodNames = Arrays.asList("start", "initialize", "shutdown","preQuery","postQuery","preInputInvocation","postInputInvocation",
                "preOutputInvocation","postInputInvocation");
        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i);
            String methodName = methodNames.get(i);
            MethodSpec method = GeneratorUtils.generateAggregatorMethods(methodName, generatedSpecs.getMethodsByType(type));
            generatedSpecs.addMethodsByTypeAndID(type, method.name,method);
        }
        return generatedSpecs;
    };

    private static CodeBlock generateThisExceptionalCase(){
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.add("case \"this\":")
                .addStatement("\nreturn this.getInputCO()");
        return builder.build();
    }
}