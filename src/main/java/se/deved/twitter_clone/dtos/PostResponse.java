package se.deved.twitter_clone.dtos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import se.deved.twitter_clone.controllers.PostController;
import se.deved.twitter_clone.controllers.UserController;
import se.deved.twitter_clone.models.Comment;
import se.deved.twitter_clone.models.Post;
import se.deved.twitter_clone.models.PostReaction;
import se.deved.twitter_clone.models.User;

import java.util.Date;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class PostResponse extends RepresentationModel<PostResponse> {

    private final UUID id;
    private String content;
    private Date createdAt;
    private UserResponse creator;
    private int likes;
    private int dislikes;

    public PostResponse(UUID id, String content, Date createdAt, User creator, int likes, int dislikes) {
        this.id = id;
        this.content = content;
        this.creator = UserResponse.fromModel(creator);
        this.createdAt = createdAt;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public static PostResponse fromModel(Post post) {
        int likes = 0;
        int dislikes = 0;
        for (var reaction : post.getReactions()) {
            if (reaction.isLiked())
                likes++;
            else dislikes++;
        }
        var response = new PostResponse(
                post.getId(),
                post.getContent(),
                post.getCreatedAt(),
                post.getCreator(),
                likes, dislikes
        );

        response.add(linkTo(
                methodOn(UserController.class).getUserById(post.getCreator().getId()))
                .withRel("user")
        );

        response.add(linkTo(
                methodOn(PostController.class).getPostById(post.getId(), false))
                .withSelfRel()
        );

        return response;
    }
}
