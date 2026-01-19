package se.deved.twitter_clone.services;

import se.deved.twitter_clone.exceptions.CreateUserException;
import se.deved.twitter_clone.models.User;

public interface IUserService {
    User createUser(String username, String password) throws CreateUserException;
}
