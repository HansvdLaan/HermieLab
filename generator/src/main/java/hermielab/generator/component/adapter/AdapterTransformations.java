package hermielab.generator.component.adapter;

import com.squareup.javapoet.*;
import hermielab.generator.component.GeneratedSpecContainer;
import hermielab.generator.component.utils.GeneratorUtils;
import hermielab.core.instancemanager.InstanceManager;
import hermielab.generator.settings.Settings;
import hermielab.generator.settings.containers.GeneratorInformationElement;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.BiFunction;

public class AdapterTransformations {

    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateConstructor = (generatedSpecs, settings) -> {
        FieldSpec instanceManager =  generatedSpecs.getFieldsByTypeAndID("instance", "hermielab/core/instancemanager");
        MethodSpec method = MethodSpec.constructorBuilder()
                .addParameter(InstanceManager.class, "instanceManager")
                .addStatement("this.$N = instanceManager",instanceManager).build();
        generatedSpecs.addMethodsByTypeAndID("constructor", "constructor", method);
        return generatedSpecs;
    };

    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateWrappedMethods = (generatedSpecs, settings) -> {
        List<String> types = Arrays.asList("parametergenerator", "predicate", "inputfunction", "outputfunction",
                "start", "initialize", "shutdown",
        "prequery","postquery","preinputinvocation","postinputinvocation","preoutputinvocation","postoutputinvocation");
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
        List<String> types = Arrays.asList("predicate", "inputfunction", "outputfunction","transform");
        List<String> methodNames = Arrays.asList("checkPredicate", "processInput", "processOutput","transformOutput");
        List<TypeName> returnTypes = Arrays.asList(TypeName.get(boolean.class),
                ParameterizedTypeName.get(Optional.class,Object.class),
                ParameterizedTypeName.get(Object.class),
                TypeName.get(Object.class));
        List<List<CodeBlock>> exceptionalCasesList = Arrays.asList(new ArrayList(), new ArrayList<>(),
                Arrays.asList(generateThisExceptionalCase()), Arrays.asList(generateToBooleanExceptionalCase(), generateToStringExceptionalCase()));
        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i);
            String methodName = methodNames.get(i);
            TypeName returnType = returnTypes.get(i);
            List<CodeBlock> exceptionalCases = exceptionalCasesList.get(i);
            MethodSpec method = GeneratorUtils.generateSwitchCaseAggregatorMethods(methodName, generatedSpecs.getMethodsByType(type), exceptionalCases, false, returnType);

            if (type.equals("transform")) {
                method = method.toBuilder().addParameter(Object.class, "output").build();
            }

            generatedSpecs.addMethodsByTypeAndID(type, method.name,method);
        }
        return generatedSpecs;
    };

    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateAggregators = (generatedSpecs, settings) -> {
        List<String> types = Arrays.asList("start", "initialize", "shutdown","prequery","postquery","preinputinvocation","postinputinvocation",
                "preoutputinvocation","postoutputinvocation");
        List<String> methodNames = Arrays.asList("start", "initialize", "shutdown","preQuery","postQuery","preInputInvocation","postInputInvocation",
                "preOutputInvocation","postOutputInvocation");
        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i);
            String methodName = methodNames.get(i);
            MethodSpec method = GeneratorUtils.generateAggregatorMethods(methodName, generatedSpecs.getMethodsByType(type));
            generatedSpecs.addMethodsByTypeAndID(type, method.name,method);
        }
        return generatedSpecs;
    };

    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateInstanceMangerField = (generatedSpecs, settings) -> {
            FieldSpec instanceMangerField = FieldSpec.builder(InstanceManager.class, "instance", Modifier.PRIVATE).build();
            generatedSpecs.addFieldsByTypeAndID("instance", "hermielab/core/instancemanager",instanceMangerField);
        return generatedSpecs;
    };

    private static CodeBlock generateThisExceptionalCase(){
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.add("case \"this\":\n")
                .add("return Optional.of(this.getInputCO());\n");
        return builder.build();
    }

    private static CodeBlock generateToStringExceptionalCase(){
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.add("case \"toString\":\n")
                .add("return output.toString();\n");
        return builder.build();
    }

    private static CodeBlock generateToBooleanExceptionalCase(){
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.add("case \"toBoolean\":\n")
                .add("return (Boolean) output;\n");
        return builder.build();
    }
}