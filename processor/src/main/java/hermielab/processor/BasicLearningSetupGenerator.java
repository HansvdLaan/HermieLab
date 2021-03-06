package hermielab.processor;

import hermielab.generator.component.ComponentGenerator;
import hermielab.generator.component.adapter.AdapterGenerator;
import hermielab.generator.component.injector.ApplicationInjectorGenerator;
import hermielab.generator.files.FileGenerator;
import hermielab.generator.files.checkers.CheckersGenerator;
import hermielab.generator.files.experiments.ExperimentsGenerator;
import hermielab.generator.files.mappings.MappingsGenerator;
import hermielab.generator.settings.Settings;
import hermielab.generator.settings.SettingsParser;
import hermielab.generator.settings.SettingsPreProcessor;
import hermielab.generator.settings.transformations.AbstractElementsTransformations;
import hermielab.generator.settings.transformations.ConcreteElementsTransformations;
import hermielab.generator.settings.transformations.SetupElementsTransformations;
import hermielab.generator.settings.transformations.UtilElementsTransformations;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.ArrayList;
import java.util.List;

public class BasicLearningSetupGenerator extends LearningSetupGenerator {

    public BasicLearningSetupGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public Settings loadSettings() {

        return SettingsParser.readSettings(SettingsParser.findSettings()).get(0);
    }

    @Override
    public List<FileGenerator> initializeFileGenerators(Settings settings) {
        Settings settingsFile = settings; //TODO: catch the case if there is none!
        List<FileGenerator> fileGenerators = new ArrayList<>();
        fileGenerators.add(new CheckersGenerator(settingsFile));
        fileGenerators.add(new ExperimentsGenerator(settingsFile));
        fileGenerators.add(new MappingsGenerator(settingsFile));
        return fileGenerators;
    }

    @Override
    public List<ComponentGenerator> initializeCompontentGenerators(Settings settings) {
        List<ComponentGenerator> componentGenerators = new ArrayList<>();
        componentGenerators.add(new AdapterGenerator(settings));
        componentGenerators.add(new ApplicationInjectorGenerator(settings));
        return componentGenerators;
    }

    @Override
    protected Settings processSettings(Settings settings) {
//            System.out.println("SETTINGS BEFORE TRANSFORMATIONS");
//            System.out.println("----------------------");
//            System.out.println(setting.toString());
//            System.out.println("----------------------");
        SettingsPreProcessor processor = new SettingsPreProcessor(settings);
        processor.addAllTransformations(
                AbstractElementsTransformations.ProcessWidgetSymbols,
                AbstractElementsTransformations.ProcessFieldMethodSymbols,
                AbstractElementsTransformations.ProcessFunctionSymbols,
                ConcreteElementsTransformations.ProcessInputWidgets,
                ConcreteElementsTransformations.ProcessInputFieldMethods,
                ConcreteElementsTransformations.ProcessInputFunctions,
                ConcreteElementsTransformations.ProcessOutputFieldMethods,
                ConcreteElementsTransformations.ProcessOutputFunctions,
                UtilElementsTransformations.ProcessParameters,
                UtilElementsTransformations.ProcessParameterGenerators,
                UtilElementsTransformations.ProcessPredicates,
                SetupElementsTransformations.ProcessStart,
                SetupElementsTransformations.ProcessInitialize,
                SetupElementsTransformations.ProcessShutdown,
                SetupElementsTransformations.ProcessPreQuery,
                SetupElementsTransformations.ProcessPostQuery,
                SetupElementsTransformations.ProcessPreInputInvocation,
                SetupElementsTransformations.ProcessPostInputInvocation,
                SetupElementsTransformations.ProcessPreOutputInvocation,
                SetupElementsTransformations.ProcessPostOutputInvocation,
                AbstractElementsTransformations.ProcessSymbolsToGenerateMappings);
        processor.applyTransformations();
//            System.out.println("SETTINGS AFTER TRANSFORMATIONS");
//            System.out.println("----------------------");
//            System.out.println(processor.getSettings().toString());
//            System.out.println("----------------------");
        return processor.getSettings();
    }
}
