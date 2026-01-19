package se.deved.twitter_clone.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.deved.twitter_clone.dtos.CreateUserRequest;
import se.deved.twitter_clone.dtos.ErrorResponse;
import se.deved.twitter_clone.dtos.UserResponse;
import se.deved.twitter_clone.exceptions.IllegalPasswordException;
import se.deved.twitter_clone.exceptions.IllegalUsernameException;
import se.deved.twitter_clone.exceptions.UserAlreadyExistsException;
import se.deved.twitter_clone.services.IUserService;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping
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
            // TODO: Implement proper logging
            exception.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("Unexpected error"));
        }
    }
}
