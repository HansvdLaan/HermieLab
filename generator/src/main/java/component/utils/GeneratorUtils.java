package component.utils;

import com.squareup.javapoet.*;
//
import com.sun.tools.javac.code.Symbol;
import settings.containers.GeneratorInformationElement;
import settings.transformations.GeneralTransformations;
import settings.transformers.ParameterGeneratorTransformer;
import utils.ClassUtils;

import javax.lang.model.element.*;
import java.util.*;

/**
 * Created by Hans on 2-2-2018.
 */
public class GeneratorUtils {

    public static MethodSpec generateSwitchCaseAggregatorMethods(String methodName, Map<String,MethodSpec> methods, boolean isVoid, TypeName returnType){
        return generateSwitchCaseAggregatorMethods(methodName,methods,new ArrayList<>(),isVoid, returnType);
    }

    public static MethodSpec generateSwitchCaseAggregatorMethods(String methodName, Map<String,MethodSpec> methods, List<CodeBlock> exceptionalCases, boolean isVoid, TypeName returnType){

        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(returnType)
                .addParameter(String.class, "ID")
                .beginControlFlow("switch(ID)");
        for (String methodID: methods.keySet()) {
            if (isVoid) {
                builder.addCode("case \"" + methodID + "\":\n")
                        .addCode(CodeBlock.builder().indent().build())
                        .addStatement("this.$N()", methods.get(methodID))
                        .addCode(CodeBlock.builder().unindent().build());
            } else if (methods.get(methodID).returnType.equals(TypeName.VOID)) {
                if (returnType.box().equals(ClassName.get(Optional.class))) {
                    builder.addCode("case \"" + methodID + "\":")
                            .addCode(CodeBlock.builder().indent().build())
                            .addStatement("this.$N()", methods.get(methodID))
                            .addStatement("return $T.empty()", Optional.class)
                            .addCode(CodeBlock.builder().unindent().build());

                }
                else {
                    builder.addCode("case \"" + methodID + "\":\n")
                            .addCode(CodeBlock.builder().indent().build())
                            .addStatement("this.$N()", methods.get(methodID))
                            .addStatement("return null")
                            .addCode(CodeBlock.builder().unindent().build());
                }
            } else {
                builder.addCode("case \"" + methodID + "\":\n")
                        .addCode(CodeBlock.builder().indent().build())
                        .addStatement("return $T.of(this.$N())", Optional.class, methods.get(methodID))
                        .addCode(CodeBlock.builder().unindent().build());
            }
        }
        for (CodeBlock exceptionalCase: exceptionalCases){
            builder.addCode(CodeBlock.builder().indent().build())
                    .addCode(exceptionalCase)
                    .addCode(CodeBlock.builder().unindent().build());
        }
        builder.addCode("default:")
                .addStatement("\nthrow new $T(\"" + methodName + ":unkown ID: \" + inputID)", IllegalArgumentException.class)
                .endControlFlow();
        return builder.build();

    }

    public static MethodSpec generateAggregatorMethods(String methodName, Map<String,MethodSpec> methods){

        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(Void.TYPE);
        for (String methodID: methods.keySet()) {
            builder.addStatement("\nthis.$N()", methods.get(methodID));
        }

        return builder.build();

    }

    public static Map<String,MethodSpec> generateWrappedMethods(Map<String,GeneratorInformationElement> elements, boolean isVoid) throws ClassNotFoundException, NoSuchMethodException {
        Map<String,MethodSpec> methods = new HashMap<>();
        for (String ID: elements.keySet()) {
            GeneratorInformationElement element = elements.get(ID);
            if (element == null) {
                methods.put(ID, generateEmptyMethod(ID, isVoid));
            } else {
                methods.put(ID, generateWrappedMethod(element, isVoid));
            }
        }
        return methods;
    }

    public static MethodSpec generateEmptyMethod(String elementID, boolean isVoid){
        String methodName = elementID.substring(0,1).toLowerCase() + elementID.substring(1);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC);
        if (isVoid){
            builder.returns(Void.TYPE);
        } else {
            builder.returns(Boolean.TYPE);
        }
        return builder.build();
    }

    public static MethodSpec generateWrappedMethod(GeneratorInformationElement element, boolean isVoid) throws ClassNotFoundException, NoSuchMethodException {
        TypeElement clazz;
        ExecutableElement method;
        VariableElement field;
        List<String> paramIDs = GeneralTransformations.getOrderedParameters(element, "param(#[0-9]*)?");
        Object[] parameters = new Object[paramIDs.size()];

        clazz = ClassUtils.getClass(element.getData().get("class").toString());
        if (element.getData().get("field") != null) {
            field = ClassUtils.getField(clazz, element.getData().get("field").toString());
            method = ClassUtils.getMethod(clazz, field, element.getData().get("method").toString());
        } else {
            method = ClassUtils.getMethod(clazz, element.getData().get("method").toString());
        }
        for (int i = 0; i < paramIDs.size(); i++) {
            String paramID = paramIDs.get(i);
            if (paramID.matches(".*:.*")){
                String paramType = paramID.split(":")[0];
                String paramValue = paramID.split(":")[1];
                if (ParameterGeneratorTransformer.SUPPORTEDPRIMITIVES.contains(paramType)){
                    parameters[i] = ParameterGeneratorTransformer.getValue(paramType,paramValue);
                } else {
                    parameters[i] = ParameterGeneratorTransformer.getMethod(paramID);
                }
            }
        }

        String methodName = element.getId().substring(0,1).toLowerCase() + element.getId().substring(1);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC);
        if (isVoid){
            builder.returns(Void.TYPE);
        } else {
            builder.returns(TypeName.get(method.getReturnType()));
        }

        builder.addCode("return instance.$L(",method.getSimpleName());
        for (int i = 0; i < parameters.length; i++){
            Object parameter = parameters[i];
            if (ClassUtils.isWrapperType(parameter.getClass())){
                builder.addCode("$L",parameter);
            } else if (parameter instanceof String) {
                builder.addCode("$S",parameter);
            } else {
                builder.addCode("$T.$L()",((Symbol.MethodSymbol) parameters[0]).owner,((Symbol.MethodSymbol) parameters[0]).name.toString());
            }
            if (i != parameters.length -1){
                builder.addCode(",");
            }
        }
        builder.addCode(");\n");

        return builder.build();
    }
}
