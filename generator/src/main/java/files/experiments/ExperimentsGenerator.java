package files.experiments;

import files.FileGenerator;

public class ExperimentsGenerator extends FileGenerator {
    
    public ExperimentsGenerator() {
        super("experiments", "settings");
        this.addTransformation(ExperimentTransformations.experimentTransformations);
    }
}
