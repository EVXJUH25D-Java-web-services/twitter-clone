package se.deved.twitter_clone.exceptions;

public class CreatePostException extends RuntimeException {
    public CreatePostException() {}

    public CreatePostException(String message) {
        super(message);
    }

    public CreatePostException(String message, Throwable inner) {
        super(message, inner);
    }
}
