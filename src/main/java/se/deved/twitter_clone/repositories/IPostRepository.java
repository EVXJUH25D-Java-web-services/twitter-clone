package se.deved.twitter_clone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.deved.twitter_clone.models.Post;

import java.util.UUID;

@Repository
public interface IPostRepository extends JpaRepository<Post, UUID> {}