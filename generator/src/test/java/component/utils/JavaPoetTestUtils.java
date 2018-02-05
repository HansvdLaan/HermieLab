package component.utils;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class JavaPoetTestUtils {

    public static boolean containsMethod(TypeSpec component, String expectedName){
        return component.methodSpecs.stream().anyMatch(method -> method.name.equals(expectedName));
    }

    public static boolean containsMethod(TypeSpec component, String expectedName, int expectedParameterCount){
        return component.methodSpecs.stream().anyMatch(method -> method.name.equals(expectedName)
                && method.parameters.size() == expectedParameterCount);
    }

    public static List<MethodSpec> getMethods(TypeSpec component, String expectedName){
        return component.methodSpecs.stream().filter(method -> method.name.equals(expectedName)).collect(Collectors.toList());
    }

    public static boolean isCorrectWrapperMethod(TypeSpec component, String expectedMethodName, String expectedWrappedMethodName,
                                                 List<String> parameters, Class returnType) {
        MethodSpec wrapperMethod = getMethods(component, expectedMethodName).get(0);
        return wrapperMethod.returnType.toString().equals(returnType.toString()) &&
                wrapperMethod.code.toString().equals(generateWrapperMethodString(expectedWrappedMethodName, parameters));
    }

    public static boolean isEmptyWrapperMethod(TypeSpec component, String expectedMethodName) {
        return getMethods(component,expectedMethodName).get(0).code.isEmpty();
    }

    public static boolean containsField(TypeSpec component, String expectedName){
        return component.methodSpecs.stream().anyMatch(method -> method.name.equals(expectedName));
    }

    public static boolean containsField(TypeSpec component, String expectedName, TypeName type){
        return component.fieldSpecs.stream().anyMatch(field -> field.name.equals(expectedName)
                && field.type == type);
    }

    private static String generateWrapperMethodString(String methodName, List<String> parameters){
        String method = "return instance." + methodName + "(";
        for (int i = 0; i < parameters.size(); i++) {
            method += parameters.get(0);
            if (i != parameters.size()-1) {
                method += ",";
            }
        }
        method += ");\n";
        return method;
    }

}
