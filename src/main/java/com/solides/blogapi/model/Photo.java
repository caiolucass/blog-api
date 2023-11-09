package com.solides.blogapi.model;

import com.solides.blogapi.model.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serial;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "photos", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
public class Photo extends UserDateAudit {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "url")
    private String url;

    @NotBlank
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    @ToString.Exclude
    private Album album;


    public Photo(@NotBlank String title, @NotBlank String url, @NotBlank String thumbnailUrl, Album album) {
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.album = album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo photo)) return false;
        return Objects.equals(getId(), photo.getId()) && Objects.equals(getTitle(),
                photo.getTitle()) && Objects.equals(getUrl(), photo.getUrl()) &&
                Objects.equals(getThumbnailUrl(), photo.getThumbnailUrl()) && Objects.equals(getAlbum(), photo.getAlbum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getUrl(), getThumbnailUrl(), getAlbum());
    }
}
