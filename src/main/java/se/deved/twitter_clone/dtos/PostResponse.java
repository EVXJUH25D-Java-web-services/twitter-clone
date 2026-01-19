package se.deved.twitter_clone.dtos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import se.deved.twitter_clone.models.Comment;
import se.deved.twitter_clone.models.Post;
import se.deved.twitter_clone.models.PostReaction;
import se.deved.twitter_clone.models.User;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class PostResponse {

    private final UUID id;
    private String content;
    private Date createdAt;
    private UserResponse creator;

    public PostResponse(UUID id, String content, Date createdAt, User creator) {
        this.id = id;
        this.content = content;
        this.creator = UserResponse.fromModel(creator);
        this.createdAt = createdAt;
    }

    public static PostResponse fromModel(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getCreatedAt(),
                post.getCreator()
        );
    }
}
