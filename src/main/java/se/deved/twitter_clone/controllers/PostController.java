package se.deved.twitter_clone.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.deved.twitter_clone.dtos.CreatePostRequest;
import se.deved.twitter_clone.dtos.ErrorResponse;
import se.deved.twitter_clone.dtos.PostResponse;
import se.deved.twitter_clone.exceptions.InvalidContentLengthException;
import se.deved.twitter_clone.exceptions.MissingUserException;
import se.deved.twitter_clone.services.IPostService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest request) {
        try {
            var post = postService.createPost(request);
            return ResponseEntity.created(URI.create("/post")).body(PostResponse.fromModel(post));
        } catch (InvalidContentLengthException ignored) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Content must be between 5 and 200 characters"));
        } catch (MissingUserException ignored) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Wrong username or password"));
        } catch (Exception exception) {
            // TODO: Implement proper logging
            exception.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("Unexpected error"));
        }
    }

    // TODO: Implement paging
    @GetMapping("/all")
    public List<PostResponse> getAllPosts() {
        return postService
                .getAllPosts()
                .stream()
                .map(PostResponse::fromModel)
                .toList();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable UUID postId) {
        return postService.getPostById(postId)
                .map(post -> ResponseEntity.ok(PostResponse.fromModel(post)))
                .orElse(ResponseEntity.notFound().build());
    }
}
