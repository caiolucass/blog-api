package com.solides.blogapi.service;

import com.solides.blogapi.payload.request.PhotoRequest;
import com.solides.blogapi.payload.response.ApiResponse;
import com.solides.blogapi.payload.response.PagedResponse;
import com.solides.blogapi.payload.response.PhotoResponse;
import com.solides.blogapi.security.UserPrincipal;

public interface PhotoService {

    PagedResponse<PhotoResponse> getAllPhotos(int page, int size);

    PhotoResponse getPhoto(Long id);

    PhotoResponse updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser);

    PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser);

    ApiResponse deletePhoto(Long id, UserPrincipal currentUser);

    PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size);

}
