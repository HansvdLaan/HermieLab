package general;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import component.ComponentGenerator;
import component.GeneratedSpecContainer;
import files.FileGenerator;
import org.dom4j.Document;
import org.dom4j.io.XMLWriter;
import settings.Settings;
import utils.ClassUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class LearningSetupGenerator {

    private Settings settings;
    private List<FileGenerator> fileGenerators;
    private List<Document> generatedDocuments;
    private List<ComponentGenerator> componentGenerators;
    private List<TypeSpec> generatedComponents;
    private ProcessingEnvironment processingEnv;

    public LearningSetupGenerator(ProcessingEnvironment processingEnv){
        this.processingEnv = processingEnv;
        ClassUtils.getInstance().setPrEnv(processingEnv);
    }

    public abstract Settings loadSettings();

    public abstract List<FileGenerator> initializeFileGenerators(Settings settings);
    
    public List<Document> generateDocument(){
        List<Document> generatedDocuments = new ArrayList<>();
        for (FileGenerator generator: fileGenerators){
            generator.applyTransformations();
            generatedDocuments.add(generator.getDocument());
        }
        return generatedDocuments;
    }

    
    public abstract List<ComponentGenerator> initializeCompontentGenerators(Settings settings) ;
    
    public List<TypeSpec> generateCompontents(){
        List<TypeSpec> generatedComponents = new ArrayList<>();
        GeneratedSpecContainer generatedSpecs = new GeneratedSpecContainer();
        for (ComponentGenerator generator: componentGenerators){
            generator.setGeneratedSpecs(generatedSpecs);
            generator.setSettings(getSettings());
            generator.applyTransformations();
            generatedSpecs = generator.getGeneratedSpecs();
            TypeSpec component = generator.generateComponent();
            generatedComponents.add(component);
        }
        return generatedComponents;
    }
    
    public void generateLearningSetup(){

        this.settings = loadSettings();
        this.settings = processSettings(settings);
        this.fileGenerators = initializeFileGenerators(getSettings());
        this.generatedDocuments = generateDocument();
        this.componentGenerators = initializeCompontentGenerators(getSettings());
        this.generatedComponents = generateCompontents();

        for (Document document: generatedDocuments){
            writeToFile(document);
        }
        for (TypeSpec typeSpec: generatedComponents){
            writeToFile(typeSpec);
        }
    }

    protected abstract Settings processSettings(Settings settings);

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public List<FileGenerator> getFileGenerators() {
        return fileGenerators;
    }

    public void setFileGenerators(List<FileGenerator> fileGenerators) {
        this.fileGenerators = fileGenerators;
    }

    public List<Document> getGeneratedDocuments() {
        return generatedDocuments;
    }

    public void setGeneratedDocuments(List<Document> generatedDocuments) {
        this.generatedDocuments = generatedDocuments;
    }

    public List<ComponentGenerator> getComponentGenerators() {
        return componentGenerators;
    }

    public void setComponentGenerators(List<ComponentGenerator> componentGenerators) {
        this.componentGenerators = componentGenerators;
    }

    public void setGeneratedComponents(List<TypeSpec> generatedComponents) {
        this.generatedComponents = generatedComponents;
    }

    public ProcessingEnvironment getProcessingEnv() {
        return processingEnv;
    }

    public void setProcessingEnv(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public void writeToFile(TypeSpec typeSpec){
        JavaFile javaFile = JavaFile.builder("hermielab", typeSpec).indent("\t").build();
        String content = javaFile.toString();
        try {
            try (Writer writer = generateResource(javaFile.packageName, javaFile.typeSpec.name + ".java").openWriter()) {
                writer.write(content);
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    public void writeToFile(Document document) {
        org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
        try {
            OutputStream outputStream = generateResource("hermielab", document.getName() + ".xml").openOutputStream();
            XMLWriter writer = new XMLWriter(outputStream, format);
            writer.write(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileObject generateResource(String packageName, String fileName) throws IOException {
        return processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, packageName, fileName);
    }

}
