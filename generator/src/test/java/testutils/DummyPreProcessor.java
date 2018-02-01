package settings.annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Hans on 22-1-2018.
 */
@SupportedAnnotationTypes("annotations.FieldTest")
public class PreProcessorUtilsTestProcessor extends AbstractProcessor{

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

    public ProcessingEnvironment getPreEnv(){
        return processingEnv;
    }
}
