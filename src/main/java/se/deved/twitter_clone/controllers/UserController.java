package se.deved.twitter_clone.controllers;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import se.deved.twitter_clone.dtos.CreateUserRequest;
import se.deved.twitter_clone.dtos.ErrorResponse;
import se.deved.twitter_clone.dtos.LoginUserRequest;
import se.deved.twitter_clone.dtos.UserResponse;
import se.deved.twitter_clone.exceptions.IllegalPasswordException;
import se.deved.twitter_clone.exceptions.IllegalUsernameException;
import se.deved.twitter_clone.exceptions.UserAlreadyExistsException;
import se.deved.twitter_clone.services.IUserService;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            var user = userService.createUser(request.getUsername(), request.getPassword());
            return ResponseEntity.created(URI.create("/user")).body(UserResponse.fromModel(user));
        } catch (IllegalPasswordException exception) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "Invalid password",
                            "errors", exception.getErrors()
                    ));
        } catch (IllegalUsernameException ignored) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Invalid username, must have at least 5 characters"));
        } catch (UserAlreadyExistsException ignored) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("That username is taken"));
        } catch (Exception exception) {
            log.error("Error creating user", exception);
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("Unexpected error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserRequest request) {
        try {
            var token = userService.authenticateUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (AuthException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Wrong username or password"));
        } catch (Exception exception) {
            log.error("Error creating user", exception);
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("Unexpected error"));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        return userService
                .getUserById(userId)
                .map(user -> ResponseEntity.ok(UserResponse.fromModel(user)))
                .orElse(ResponseEntity.notFound().build());
    }
}
