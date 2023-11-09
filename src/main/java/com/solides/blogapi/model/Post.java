package com.solides.blogapi.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.solides.blogapi.model.audit.UserDateAudit;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Post extends UserDateAudit {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(getId(), post.getId()) && Objects.equals(getTitle(),
                post.getTitle()) && Objects.equals(getBody(), post.getBody())
                && Objects.equals(getUser(), post.getUser()) && Objects.equals(getComments(), post.getComments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getBody(), getUser(), getComments());
    }
}

