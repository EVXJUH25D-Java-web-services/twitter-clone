package se.deved.twitter_clone.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IllegalPasswordException extends CreateUserException {
    
    private List<PasswordError> errors;
    
    public IllegalPasswordException(List<PasswordError> errors) {
        this.errors = errors;
    }
    
}
