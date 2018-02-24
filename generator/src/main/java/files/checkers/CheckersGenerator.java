package files.checkers;

import files.FileGenerator;

public class CheckersGenerator extends FileGenerator {

    public CheckersGenerator() {
        super("checkers", "settings");
        this.addTransformation(CheckerTransformations.nfaCheckerTransformation);
        this.addTransformation(CheckerTransformations.predicateCheckerTransformation);
    }
}
