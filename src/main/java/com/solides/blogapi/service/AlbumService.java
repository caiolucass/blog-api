package com.solides.blogapi.service;

import com.solides.blogapi.model.Album;
import com.solides.blogapi.payload.request.AlbumRequest;
import com.solides.blogapi.payload.response.AlbumResponse;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.payload.response.PagedResponse;
import com.solides.blogapi.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface AlbumService {

    PagedResponse<AlbumResponse> getAllAlbums(int page, int size);

    ResponseEntity<Album> addAlbum(AlbumRequest albumRequest, UserPrincipal currentUser);

    ResponseEntity<Album> getAlbum(Long id);

    ResponseEntity<AlbumResponse> updateAlbum(Long id, AlbumRequest newAlbum, UserPrincipal currentUser);

    ResponseEntity<ApiResponse> deleteAlbum(Long id, UserPrincipal currentUser);

    PagedResponse<Album> getUserAlbums(String username, int page, int size);

}
