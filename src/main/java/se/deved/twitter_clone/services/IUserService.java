package se.deved.twitter_clone.services;

import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.userdetails.UserDetailsService;
import se.deved.twitter_clone.dtos.LoginUserRequest;
import se.deved.twitter_clone.exceptions.CreateUserException;
import se.deved.twitter_clone.models.User;

import java.util.Optional;
import java.util.UUID;

public interface IUserService extends UserDetailsService {
    User createUser(String username, String password) throws CreateUserException;
    User createOidcUser(String username, String oidcId, String oidcProvider) throws CreateUserException;
    String authenticateUser(String username, String password) throws AuthException;
    Optional<User> getUserById(UUID userId);
    Optional<User> getUserByOidc(String oidcId, String oidcProvider);
}
