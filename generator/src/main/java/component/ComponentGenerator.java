package component;

import com.squareup.javapoet.TypeSpec;
import settings.Settings;

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
    private List<String> methodTypeOrder;
    private List<String> fieldTypeOrder;

    private TypeSpec component;

    public ComponentGenerator(Settings settings, String className){
        transformations = new ArrayList<>();
        this.generatedSpecs = new GeneratedSpecContainer();
        this.settings = settings;
        this.className = className;
        this.methodTypeOrder = new ArrayList<>();
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
}
