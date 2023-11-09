package com.solides.blogapi.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.solides.blogapi.model.Photo;
import com.solides.blogapi.model.User;
import com.solides.blogapi.payload.UserDateAuditPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlbumResponse extends UserDateAuditPayload {

    private UUID id;

    private String title;

    private User user;

    private List<Photo> photo;

    public List<Photo> getPhoto() {

        return photo == null ? null : new ArrayList<>(photo);
    }

    public void setPhoto(List<Photo> photo) {

        if (photo == null) {
            this.photo = null;
        } else {
            this.photo = Collections.unmodifiableList(photo);
        }
    }
}
