package processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Created by Hans on 24-2-2018.
 */
public class BasicLearningSetupProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        BasicLearningSetupGenerator generator = new BasicLearningSetupGenerator(this.processingEnv);
        generator.generateLearningSetup();
        return true;
    }
}
