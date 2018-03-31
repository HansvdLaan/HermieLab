package component;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import settings.Settings;
import settings.containers.GeneratorInformationElement;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.BiFunction;

public class ComponentGenerator {

    private List<BiFunction<
            GeneratedSpecContainer, //the original settings
            Settings, //the preprocessed settings upon that point
            GeneratedSpecContainer> //the preprocessed settings after the transformation
            > transformations;
    private GeneratedSpecContainer generatedSpecs;
    private Settings settings;

    private String className;
    private String packageName;
    private List<String> methodTypeOrder;
    private List<String> fieldTypeOrder;
    private Class superClass;
    private List<Class> interfaces;

    public ComponentGenerator(Settings settings, String packageName, String className){
        interfaces = new LinkedList<>();
        transformations = new ArrayList<>();
        this.generatedSpecs = new GeneratedSpecContainer();
        this.className = className;
        this.packageName = packageName;
        this.methodTypeOrder = new ArrayList<>();
        this.settings = settings;
        this.settings.addElement(new GeneratorInformationElement("componentgenerator",className.toLowerCase(),
                null,
                "className", className,
                "packageName",packageName));
    }

    public List<BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer>> getTransformations() {
        return transformations;
    }

    public void setTransformations(List<BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer>> transformations) {
        this.transformations = transformations;
    }

    public void addTransformation(BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer> transformation){
        this.transformations.add(transformation);
    }

    public void addAllTransformations(Collection<BiFunction<GeneratedSpecContainer,Settings, GeneratedSpecContainer>> transformations){
        this.transformations.addAll(transformations);
    }

    public void applyTransformations(){
        for (BiFunction<GeneratedSpecContainer, Settings, GeneratedSpecContainer> function: transformations){
            function.apply(generatedSpecs, settings);
        }
    }

    public TypeSpec generateComponent() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(className);
        builder.addModifiers(Modifier.PUBLIC);
        for (String type: getFieldTypeOrder()){
            List<String> IDs = new ArrayList<>(generatedSpecs.getFieldsByType(type).keySet());
            Collections.sort(IDs);
            IDs.forEach(id -> builder.addField(getGeneratedSpecs().getFieldsByTypeAndID(type,id)));
        }

        for (String type: getMethodTypeOrder()){
            List<String> IDs = new ArrayList<>(generatedSpecs.getMethodsByType(type).keySet());
            Collections.sort(IDs);
            IDs.forEach(id -> builder.addMethod(getGeneratedSpecs().getMethodsByTypeAndID(type,id)));
        }
        if (getSuperClass() != null) {
            builder.superclass(getSuperClass());
        }
        if (getInterfaces().size() > 0) {
            getInterfaces().forEach(builder::addSuperinterface);
        }
        return builder.build();
    }

    public GeneratedSpecContainer getGeneratedSpecs() {
        return generatedSpecs;
    }

    public void setGeneratedSpecs(GeneratedSpecContainer generatedSpecs) {
        this.generatedSpecs = generatedSpecs;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }


    public List<String> getMethodTypeOrder() {
        return methodTypeOrder;
    }

    public void setMethodTypeOrder(List<String> methodTypeOrder) {
        this.methodTypeOrder = methodTypeOrder;
    }

    public List<String> getFieldTypeOrder() {
        return fieldTypeOrder;
    }

    public void setFieldTypeOrder(List<String> fieldTypeOrder) {
        this.fieldTypeOrder = fieldTypeOrder;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Class getSuperClass() {
        return superClass;
    }

    public void setSuperClass(Class superClass) {
        this.superClass = superClass;
    }

    public List<Class> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<Class> interfaces) {
        this.interfaces = interfaces;
    }
}
