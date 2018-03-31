package files.checkers;

import files.FileGenerator;
import settings.Settings;

public class CheckersGenerator extends FileGenerator {

    public CheckersGenerator(Settings settings) {
        super("checkers", "checkers");
        setSettings(settings);
        this.addTransformation(CheckerTransformations.nfaCheckerTransformation);
        this.addTransformation(CheckerTransformations.predicateCheckerTransformation);
    }
}
