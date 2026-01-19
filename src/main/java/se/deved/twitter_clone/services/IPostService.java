package se.deved.twitter_clone.services;

import se.deved.twitter_clone.dtos.CreatePostRequest;
import se.deved.twitter_clone.exceptions.CreatePostException;
import se.deved.twitter_clone.models.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPostService {

    Post createPost(CreatePostRequest request) throws CreatePostException;
    List<Post> getAllPosts();
    Optional<Post> getPostById(UUID postId);
}
