package settings;

import java.util.*;
import java.util.function.BiFunction;

public class SettingsPreProcessor {

    private List<BiFunction<
            Settings, //the original settings
            Settings, //the preprocessed settings upon that point
            Settings> //the preprocessed settings after the transformation
            > transformations;
    private Settings init;
    private Settings settings;

    public SettingsPreProcessor(Settings init){
        transformations = new ArrayList<>();
        this.init = init;
        this.settings = new Settings();
    }

    public List<BiFunction<Settings,Settings,Settings>> getTransformations() {
        return transformations;
    }

    public void setTransformations(List<BiFunction<Settings,Settings,Settings>> transformations) {
        this.transformations = transformations;
    }

    public void addTransformation(BiFunction<Settings,Settings,Settings> transformation){
        this.transformations.add(transformation);
    }

    public void addAllTransformations(Collection<BiFunction<Settings,Settings,Settings>> transformations){
        this.transformations.addAll(transformations);
    }

    public void applyTransformations(){
        for (BiFunction<Settings, Settings, Settings> function: transformations){
            function.apply(init, settings);
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
