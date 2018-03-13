package component.injector;

import com.squareup.javapoet.*;
import component.GeneratedSpecContainer;
import component.utils.GeneratorUtils;
import javassist.*;
import settings.Settings;
import settings.containers.GeneratorInformationElement;
import utils.ClassUtils;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Created by Hans on 1-3-2018.
 */
public class ApplicationInjectorTransformations {


    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateConstructor = (generatedSpecs, settings) -> {
        FieldSpec observedClasses = generatedSpecs.getFieldsByTypeAndID("instancefield","observedClasses");
        MethodSpec fillObservedClassSet = generatedSpecs.getMethodsByTypeAndID("instancemethod","fillObservedClassSet");
        MethodSpec constructor =  MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$N = new $T()",observedClasses, HashSet.class)
                .addStatement("$N()", fillObservedClassSet)
                .build();
        generatedSpecs.addMethodsByTypeAndID("constructor", "constructor", constructor);
        return generatedSpecs;
    };

    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateInjectMethod = (generatedSpecs, settings) -> {
        ClassName className = GeneratorUtils.getClassName(settings,ApplicationInjectorGenerator.CLASSNAME);
        FieldSpec observedClasses = generatedSpecs.getFieldsByTypeAndID("instancefield","observedClasses");
        MethodSpec method =   MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addException(NotFoundException.class)
                .addException(CannotCompileException.class)

                .addStatement("$T pool = $T.getDefault()", ClassPool.class, ClassPool.class)
                .beginControlFlow("for($T clazz: $N)",String.class, observedClasses)
                .addStatement("$T ctClass = pool.get(clazz)", CtClass.class)
                .beginControlFlow("for($T constructor: $N.getConstructors())", CtConstructor.class, "ctClass")
                .addStatement("constructor.insertAfter(\"{instancemanager.InstanceManager.getInstance().addClassInstance($$0);}\")", className)
                .endControlFlow()
                .addStatement("ctClass.toClass()")
                .endControlFlow()
                .build();
        generatedSpecs.addMethodsByTypeAndID("instancemethod","inject",method);
        return generatedSpecs;
    };

    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateFillObservedClassesSetMethod = (generatedSpecs, settings) -> {
        FieldSpec observedClasses = generatedSpecs.getFieldsByTypeAndID("instancefield","observedClasses");
        Set<TypeElement> observedClassesTypes = getAllObservedClasses(settings);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("fillObservedClassSet")
                .addModifiers(Modifier.PRIVATE)
                .returns(void.class);
        for (TypeElement c: observedClassesTypes){
            builder.addStatement("$N.add($S)",observedClasses,c.getQualifiedName());
        }
        MethodSpec method = builder.build();
        generatedSpecs.addMethodsByTypeAndID("instancemethod","fillObservedClassSet",method);
        return generatedSpecs;
    };

    public static BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> GenerateObservedClasssesField = (generatedSpecs, settings) -> {
        FieldSpec field = FieldSpec.builder(ParameterizedTypeName.get(Set.class,String.class),"observedClasses", Modifier.PRIVATE).build();
        generatedSpecs.addFieldsByTypeAndID("instancefield","observedClasses",field);
        return generatedSpecs;
    };

    public static Set<TypeElement> getAllObservedClasses(Settings settings){
        Set<TypeElement> observedClasses = new HashSet<>();
        List<String> types = Arrays.asList("parametergenerator", "predicate", "inputfunction", "outputfunction", "start", "initialize", "shutdown",
                "prequery","postquery","preinputinvocation","postinputinvocation","preoutputinvocation","postoutputinvocation");
        for (String type: types){
            for (GeneratorInformationElement element: settings.getSettingsByType(type).values()) {
                if (element != null) { //They could be references
                    if (element.hasAttribute("class")) {
                        String clazz = element.getStringAttribute("class").get(0);
                        try {
                            observedClasses.add(ClassUtils.getClass(clazz));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return observedClasses;
    }
}