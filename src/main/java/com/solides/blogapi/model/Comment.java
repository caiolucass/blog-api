package com.solides.blogapi.model;

import com.solides.blogapi.model.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends UserDateAudit {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank
    @Size(min = 4, max = 50)
    private String name;

    @Column(name = "email")
    @NotBlank
    @Email
    @Size(min = 4, max = 50)
    private String email;

    @Column(name = "body")
    @NotBlank
    @Size(min = 10, message = "O comentario deve ter no minimo 10 carateres.")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    public Comment(@NotBlank @Size(min = 10, message = "O comentario deve ter no minimo 10 carateres.") String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return Objects.equals(getId(), comment.getId()) && Objects.equals(getName(),
                comment.getName()) && Objects.equals(getEmail(), comment.getEmail()) &&
                Objects.equals(getBody(), comment.getBody()) && Objects.equals(getPost(),
                comment.getPost()) && Objects.equals(getUser(), comment.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getBody(), getPost(), getUser());
    }
}
