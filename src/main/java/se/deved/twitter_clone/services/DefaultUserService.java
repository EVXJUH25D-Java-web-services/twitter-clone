package se.deved.twitter_clone.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.deved.twitter_clone.exceptions.*;
import se.deved.twitter_clone.models.User;
import se.deved.twitter_clone.repositories.IUserRepository;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements IUserService {

    private final IUserRepository userRepository;

    @Override
    public User createUser(String username, String password) throws CreateUserException {
        if (username.isBlank() || username.length() < 4) {
            throw new IllegalUsernameException();
        }
        
        var passwordErrors = new ArrayList<PasswordError>();
        if (password.isBlank() || password.length() < 8) {
            passwordErrors.add(PasswordError.TOO_SHORT);
        }

        if (!password.matches(".*[A-Z].*")) {
            passwordErrors.add(PasswordError.REQUIRES_ONE_UPPERCASE);
        }

        if (!password.matches(".*\\d.*")) {
            passwordErrors.add(PasswordError.REQUIRES_ONE_NUMBER);
        }
        
        if (!passwordErrors.isEmpty()) {
            throw new IllegalPasswordException(passwordErrors);
        }
        
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        
        var user = new User(username, password);
        user = userRepository.save(user);
        // TODO: Implement proper logging
        System.out.println("User '" + user.getId() + "' with name '" + user.getUsername() + "' created.");
        
        return user;
    }
}
