package se.deved.twitter_clone.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreatePostRequest {
    private String content;
    private String username;
    private String password;
}
