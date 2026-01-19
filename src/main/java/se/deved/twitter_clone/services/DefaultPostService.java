package se.deved.twitter_clone.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.deved.twitter_clone.dtos.CreatePostRequest;
import se.deved.twitter_clone.exceptions.CreatePostException;
import se.deved.twitter_clone.exceptions.InvalidContentLengthException;
import se.deved.twitter_clone.exceptions.MissingUserException;
import se.deved.twitter_clone.models.Post;
import se.deved.twitter_clone.repositories.IPostRepository;
import se.deved.twitter_clone.repositories.IUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultPostService implements IPostService {

    private final IPostRepository postRepository;
    private final IUserRepository userRepository;

    @Override
    public Post createPost(CreatePostRequest request) throws CreatePostException {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(MissingUserException::new);

        if (!user.getPassword().equals(request.getPassword())) {
            throw new MissingUserException();
        }

        if (request.getContent().length() < 5 || request.getContent().length() > 200) {
            throw new InvalidContentLengthException();
        }

        var post = new Post(request.getContent(), user);
        post = postRepository.save(post);
        System.out.println("Post with id '" + post.getId() + "' created");
        return post;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> getPostById(UUID postId) {
        return postRepository.findById(postId);
    }
}
