package files.experiments;

import files.FileGenerator;
import settings.Settings;

public class ExperimentsGenerator extends FileGenerator {
    
    public ExperimentsGenerator(Settings settings) {
        super("experiments", "settings");
        setSettings(settings);
        this.addTransformation(ExperimentTransformations.experimentTransformations);
    }
}
