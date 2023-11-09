package com.solides.blogapi.model;

import com.solides.blogapi.model.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "albums", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
public class Album extends UserDateAudit {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Photo> photo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album album)) return false;
        return Objects.equals(getId(), album.getId()) && Objects.equals(getTitle(),
                album.getTitle()) && Objects.equals(getUser(), album.getUser()) && Objects.equals(getPhoto(), album.getPhoto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getUser(), getPhoto());
    }
}

