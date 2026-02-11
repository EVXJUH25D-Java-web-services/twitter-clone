package se.deved.twitter_clone.services;

import jakarta.annotation.Nonnull;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.deved.twitter_clone.exceptions.*;
import se.deved.twitter_clone.models.User;
import se.deved.twitter_clone.repositories.IUserRepository;
import se.deved.twitter_clone.security.JwtService;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserService implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

        password = passwordEncoder.encode(password);

        var user = new User(username, password);
        user = userRepository.save(user);
        log.info("User '{}' with name '{}' created.", user.getId(), user.getUsername());

        return user;
    }

    @Override
    public User createOidcUser(String username, String oidcId, String oidcProvider) throws CreateUserException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        var user = new User(username, null);
        user.setOidcId(oidcId);
        user.setOidcProvider(oidcProvider);

        user = userRepository.save(user);
        log.info("OIDC User '{}' with name '{}' created.", user.getId(), user.getUsername());

        return user;
    }

    @Override
    public String authenticateUser(String username, String password) throws AuthException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(AuthException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException();
        }

        return jwtService.generateToken(user.getId());
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByOidc(String oidcId, String oidcProvider) {
        return userRepository.findByOidcIdAndOidcProvider(oidcId, oidcProvider);
    }

    @Override
    @Nonnull
    public UserDetails loadUserByUsername(@Nonnull String username)
            throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }
}
