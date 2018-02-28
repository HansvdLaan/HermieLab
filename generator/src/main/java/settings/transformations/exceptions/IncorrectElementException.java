package settings.transformations.exceptions;

public class IncorrectElementException extends IllegalArgumentException {

    public IncorrectElementException() { super(); }

    public IncorrectElementException(String message) { super(message); }

    public IncorrectElementException(String message, Throwable cause) { super(message, cause); }

    public IncorrectElementException(Throwable cause) { super(cause); }
}
