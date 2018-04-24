package hermielab.generator.files.checkers;

import hermielab.generator.files.FileGenerator;
import hermielab.generator.settings.Settings;

public class CheckersGenerator extends FileGenerator {

    public CheckersGenerator(Settings settings) {
        super("checkers", "checkers");
        setSettings(settings);
        this.addTransformation(CheckerTransformations.nfaCheckerTransformation);
        this.addTransformation(CheckerTransformations.predicateCheckerTransformation);
    }
}
