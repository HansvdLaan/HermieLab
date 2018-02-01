package files.checkers;

import files.FileGenerator;

public class CheckersGenerator extends FileGenerator {

    public CheckersGenerator(String documentName, String rootName) {
        super(documentName, rootName);
        this.addTransformation(CheckerTransformations.nfaCheckerTransformation);
        this.addTransformation(CheckerTransformations.predicateCheckerTransformation);
    }
}
