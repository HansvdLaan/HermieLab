package hermielab.generator.files.experiments;

import hermielab.generator.files.FileGenerator;
import hermielab.generator.settings.Settings;

public class ExperimentsGenerator extends FileGenerator {
    
    public ExperimentsGenerator(Settings settings) {
        super("experiments", "experiments");
        setSettings(settings);
        this.addTransformation(ExperimentTransformations.experimentTransformations);
    }
}
