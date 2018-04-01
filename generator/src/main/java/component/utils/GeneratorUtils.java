package component.utils;

import com.google.common.annotations.VisibleForTesting;
import com.google.errorprone.annotations.Var;
import com.squareup.javapoet.*;
//
import com.sun.tools.javac.code.Symbol;
import settings.Settings;
import settings.containers.GeneratorInformationElement;
import settings.transformers.ParameterGeneratorTransformer;
import utils.ClassUtils;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.*;

/**
 * Created by Hans on 2-2-2018.
 */
public class GeneratorUtils {

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
                if (returnType.box().equals(ParameterizedTypeName.get(Optional.class,Object.class))) {
                    builder.addCode("case \"" + methodID + "\":\n")
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
            } else if (returnType.box().equals(ParameterizedTypeName.get(Optional.class,Object.class))){
                builder.addCode("case \"" + methodID + "\":\n")
                        .addCode(CodeBlock.builder().indent().build())
                        .addStatement("return $T.of(this.$N())", Optional.class, methods.get(methodID))
                        .addCode(CodeBlock.builder().unindent().build());
            } else {
                builder.addCode("case \"" + methodID + "\":\n")
                        .addCode(CodeBlock.builder().indent().build())
                        .addStatement("return this.$N()", methods.get(methodID))
                        .addCode(CodeBlock.builder().unindent().build());
            }
        }
        for (CodeBlock exceptionalCase: exceptionalCases){
            builder.addCode(exceptionalCase);
        }
        builder.addCode("default:\n")
                .addCode(CodeBlock.builder().indent().build())
                .addStatement("throw new $T(\"" + methodName + ":unkown ID:\" + ID)", IllegalArgumentException.class)
                .addCode(CodeBlock.builder().unindent().build())
                .endControlFlow();
        return builder.build();

    }

    public static MethodSpec generateAggregatorMethods(String methodName, Map<String,MethodSpec> methods){

        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(Void.TYPE);
        for (String methodID: methods.keySet()) {
            builder.addStatement("this.$N()", methods.get(methodID));
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
        Element reflectionElement; //Can be either a ExecutableElement of a VariableElement
        ExecutableElement method;
        VariableElement field = null;
        List<String> paramIDs = element.getStringAttribute("parameter");
        Object[] parameters = new Object[paramIDs.size()];

        String classString = element.getStringAttribute("class").get(0);
        clazz = ClassUtils.getClass(classString);
        if (element.getAttribute("method").size() > 0) {
            String methodString = element.getStringAttribute("method").get(0);
            if (element.getAttribute("field").size() != 0) {
                field = ClassUtils.getField(clazz, element.getStringAttribute("field").get(0));
                reflectionElement = ClassUtils.getMethod(clazz, field, methodString);
            } else {
                reflectionElement = ClassUtils.getMethod(clazz, methodString);
            }
        } else if (element.getAttribute("field").size() > 0) {
            reflectionElement = ClassUtils.getField(clazz, element.getStringAttribute("field").get(0));
        } else {
            throw new IllegalArgumentException("element " + element.getType() + ":" + element.getID() +
                    " should either contain a method or a field attribute to generate a wrapped method for it");
        }
        for (int i = 0; i < paramIDs.size(); i++) {
            String paramID = paramIDs.get(i);
            if (paramID.matches(".*:.*")){
                String paramType = paramID.split(":")[0];
                String paramValue = paramID.split(":")[1];
                if (ParameterGeneratorTransformer.SUPPORTEDPRIMITIVES.contains(paramType)){
                    parameters[i] = ParameterGeneratorTransformer.getValue(paramType,paramValue);
                } else if (ParameterGeneratorTransformer.getMethod(paramID) != null){
                    parameters[i] = ParameterGeneratorTransformer.getMethod(paramID);
                } else {
                    throw new IllegalArgumentException("Unkown library and/or parameter generator " + paramID);
                }
            } else {
                parameters[i] = Optional.of(paramID); //TODO: create a new wrapper specialy for this case
            }
        }

        String methodName = element.getID().substring(0,1).toLowerCase() + element.getID().substring(1);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC);
        if (isVoid){
            builder.returns(Void.TYPE);
        } else {
            if (reflectionElement instanceof ExecutableElement) {
                TypeMirror returnType = ((ExecutableElement) reflectionElement).getReturnType();
                builder.returns(TypeName.get( ((ExecutableElement) reflectionElement).getReturnType()));
            } else if (reflectionElement instanceof VariableElement) {
                builder.returns(TypeName.get( ((VariableElement) reflectionElement).asType()));
            } else {
                throw new IllegalArgumentException("the found reflection element should either be of type ExecutableElement of VariableElement");
            }
            builder.addCode("return ");
        }

        if (reflectionElement instanceof ExecutableElement) {
            if (field == null) {
                builder.addCode("(($T) instance.getClassInstance($T.class)).$L(", clazz, clazz, reflectionElement.getSimpleName());
            } else {
                builder.addCode("(($T) instance.getClassInstance($T.class)).$L.$L(", clazz, clazz, field, reflectionElement.getSimpleName());
            }
        } else if (reflectionElement instanceof VariableElement) {
            builder.addCode("(($T) instance.getClassInstance($T.class)).$L",clazz,clazz,reflectionElement.getSimpleName());
        }

        if (reflectionElement instanceof ExecutableElement) {
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                if (ClassUtils.isWrapperType(parameter.getClass())) {
                    builder.addCode("$L", parameter);
                } else if (parameter instanceof String) {
                    builder.addCode("$S", parameter);
                } else if (parameter instanceof Optional) {
                    builder.addCode("$L()", ((Optional) parameter).get());
                } else {
                    builder.addCode("$T.$L()", ((Symbol.MethodSymbol) parameters[0]).owner, ((Symbol.MethodSymbol) parameters[0]).name.toString());
                }
                if (i != parameters.length - 1) {
                    builder.addCode(",");
                }
            }
            builder.addCode(")");
        }
        builder.addCode(";\n");
        return builder.build();
    }

    public static ClassName getClassName(Settings settings, String className){
        GeneratorInformationElement element = settings.getSettingsByTypeAndID("componentgenerator",className.toLowerCase());
        String packageName = element.getStringAttribute("packageName").get(0);
        return ClassName.get(packageName, className);
    }
}
