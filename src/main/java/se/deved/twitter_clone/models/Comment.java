package se.deved.twitter_clone.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity(name = "comments")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    
    @Id
    private final UUID id = UUID.randomUUID();
    
    private String content;
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    
    public Comment(String content, User creator, Post post) {
        this.content = content;
        this.creator = creator;
        this.post = post;
        this.createdAt = new Date();
    }
}
