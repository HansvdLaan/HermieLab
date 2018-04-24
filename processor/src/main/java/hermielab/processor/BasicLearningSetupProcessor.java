package hermielab.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;
import hermielab.generator.settings.JavaFXPredicates;

@SupportedAnnotationTypes("hermielab.annotations.setup.Start")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BasicLearningSetupProcessor extends AbstractProcessor {

    boolean initialProcessing;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        initialProcessing = false;
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Basic Learning Setup Processor instantiated");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing...");
        if (!initialProcessing) {
            BasicLearningSetupGenerator generator = new BasicLearningSetupGenerator(this.processingEnv);
            generator.generateLearningSetup();
            initialProcessing = true;
        }
        return initialProcessing;
    }
}
