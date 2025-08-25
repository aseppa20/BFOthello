package bfothello;

public class BadHashException extends Exception {
    public BadHashException(String message) {
        super(message);
    }
    public BadHashException(String message, Throwable cause) {
        super(message, cause);
    }
}
