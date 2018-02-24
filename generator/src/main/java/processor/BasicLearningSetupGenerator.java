package processor;

import component.ComponentGenerator;
import component.adapter.AdapterGenerator;
import files.FileGenerator;
import files.checkers.CheckersGenerator;
import files.experiments.ExperimentsGenerator;
import files.mappings.MappingsGenerator;

import javax.annotation.processing.ProcessingEnvironment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans on 24-2-2018.
 */
public class BasicLearningSetupGenerator extends LearningSetupGenerator {

    public BasicLearningSetupGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public List<FileGenerator> initializeFileGenerators() {
        List<FileGenerator> fileGenerators = new ArrayList<>();
        fileGenerators.add(new CheckersGenerator());
        fileGenerators.add(new ExperimentsGenerator());
        fileGenerators.add(new MappingsGenerator());
        return fileGenerators;
    }

    @Override
    public List<ComponentGenerator> initializeCompontentGenerators() {
        List<ComponentGenerator> componentGenerators = new ArrayList<>();
        componentGenerators.add(new AdapterGenerator());
        return componentGenerators;
    }
}
