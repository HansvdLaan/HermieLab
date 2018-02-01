package files.mappings;

import files.FileGenerator;

public class MappingsGenerator extends FileGenerator {

    public MappingsGenerator(String documentName, String rootName) {
        super(documentName, rootName);
        addTransformation(MappingsTransformations.abstractSymbolTransformation);
    }
}
