package basic;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("annotations.setup.Start")
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
        if (!initialProcessing) {
            basic.BasicLearningSetupGenerator generator = new basic.BasicLearningSetupGenerator(this.processingEnv);
            generator.generateLearningSetup();
            initialProcessing = true;
        }
        return initialProcessing;
    }
}
