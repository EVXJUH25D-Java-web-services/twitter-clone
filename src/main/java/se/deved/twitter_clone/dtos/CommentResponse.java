package se.deved.twitter_clone.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import se.deved.twitter_clone.controllers.PostController;
import se.deved.twitter_clone.controllers.UserController;
import se.deved.twitter_clone.models.Comment;
import se.deved.twitter_clone.models.User;

import java.util.Date;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class CommentResponse extends RepresentationModel<CommentResponse> {

    private final UUID id;
    private String content;
    private Date createdAt;
    private UserResponse creator;
    private UUID postId;

    public CommentResponse(UUID id, String content, Date createdAt, User user, UUID postId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.creator = UserResponse.fromModel(user);
        this.postId = postId;
    }

    public static CommentResponse fromModel(Comment comment) {
        var response = new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getCreator(),
                comment.getPost().getId()
        );

        response.add(linkTo(
                methodOn(PostController.class).getPostById(comment.getPost().getId(), false))
                .withRel("post")
        );

        response.add(linkTo(
                methodOn(UserController.class).getUserById(comment.getCreator().getId()))
                .withRel("user")
        );

        return response;
    }
}
