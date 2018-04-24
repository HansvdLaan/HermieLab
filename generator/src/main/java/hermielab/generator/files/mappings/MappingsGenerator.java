package hermielab.generator.files.mappings;

import hermielab.generator.files.FileGenerator;
import hermielab.generator.settings.Settings;

public class MappingsGenerator extends FileGenerator {

    public MappingsGenerator(Settings settings) {
        super("mappings", "mappings");
        setSettings(settings);
        addTransformation(MappingsTransformations.abstractSymbolTransformation);
    }
}
