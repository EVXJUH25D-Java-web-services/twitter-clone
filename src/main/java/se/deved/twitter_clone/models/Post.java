package se.deved.twitter_clone.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    private final UUID id = UUID.randomUUID();

    private String content;
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostReaction> reactions = new ArrayList<>();
    
    public Post(String content, User creator) {
        this.content = content;
        this.creator = creator;
        this.createdAt = new Date();
    }
}
