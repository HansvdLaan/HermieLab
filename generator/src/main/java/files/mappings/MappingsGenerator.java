package files.mappings;

import files.FileGenerator;

public class MappingsGenerator extends FileGenerator {

    public MappingsGenerator() {
        super("mappings", "settings");
        addTransformation(MappingsTransformations.abstractSymbolTransformation);
    }
}
