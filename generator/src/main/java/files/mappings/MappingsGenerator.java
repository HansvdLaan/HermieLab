package files.mappings;

import files.FileGenerator;
import settings.Settings;

public class MappingsGenerator extends FileGenerator {

    public MappingsGenerator(Settings settings) {
        super("mappings", "mappings");
        setSettings(settings);
        addTransformation(MappingsTransformations.abstractSymbolTransformation);
    }
}
