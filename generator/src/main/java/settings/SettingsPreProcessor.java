package settings;

import settings.transformations.TransformationResult;

import java.util.*;
import java.util.function.BiFunction;

public class SettingsPreProcessor {

    private List<BiFunction<
            Settings, //the original settings
            Settings, //the preprocessed settings upon that point
            TransformationResult> //the preprocessed settings after the transformation
            > transformations;
    private Settings init;
    private Settings settings;

    public SettingsPreProcessor(Settings init){
        transformations = new ArrayList<>();
        this.init = init;
        this.settings = new Settings();
    }

    public List<BiFunction<Settings,Settings,TransformationResult>> getTransformations() {
        return transformations;
    }

    public void setTransformations(List<BiFunction<Settings,Settings,TransformationResult>> transformations) {
        this.transformations = transformations;
    }

    public void addTransformation(BiFunction<Settings,Settings,TransformationResult> transformation){
        this.transformations.add(transformation);
    }

    public void addAllTransformations(Collection<BiFunction<Settings,Settings,TransformationResult>> transformations){
        this.transformations.addAll(transformations);
    }

    public void addAllTransformations(BiFunction<Settings,Settings,TransformationResult>... transformations){
        for (BiFunction transformation: transformations){
            this.addTransformation(transformation);
        }
    }

    public void applyTransformations(){
        TransformationResult result;
        for (BiFunction<Settings, Settings, TransformationResult> function: transformations){
            result = function.apply(init, settings);
            //TODO: Logging of results
        }
    }

    public Settings getInit() {
        return init;
    }

    public void setInit(Settings init) {
        this.init = init;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
