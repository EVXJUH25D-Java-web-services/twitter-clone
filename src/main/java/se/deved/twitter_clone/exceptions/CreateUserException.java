package se.deved.twitter_clone.exceptions;

public class CreateUserException extends RuntimeException {
    public CreateUserException() {}
    
    public CreateUserException(String message) {
        super(message);
    }

    public CreateUserException(String message, Throwable inner) {
        super(message, inner);
    }
}
