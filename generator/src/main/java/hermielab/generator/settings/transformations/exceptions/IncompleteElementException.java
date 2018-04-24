package hermielab.generator.settings.transformations.exceptions;

public class IncompleteElementException extends IllegalArgumentException {

    public IncompleteElementException() { super(); }

    public IncompleteElementException(String message) { super(message); }

    public IncompleteElementException(String message, Throwable cause) { super(message, cause); }

    public IncompleteElementException(Throwable cause) { super(cause); }
}
