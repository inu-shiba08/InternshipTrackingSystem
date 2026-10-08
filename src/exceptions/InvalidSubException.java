package exceptions;

/**
 * Thrown when a progress submission or related workflow contains invalid data.
 */
public class InvalidSubException extends Exception {

    public InvalidSubException(String message) {
        super(message);
    }
}
